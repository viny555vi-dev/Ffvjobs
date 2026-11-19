package com.ffvreborn.abilities;
import com.ffvreborn.core.*;
import java.util.*;
public final class AbilityService {
    public enum SlotType { ACTIVE, PASSIVE }
    public record CastResult(boolean success, String reason) { }
    private final Map<String, AbilityDefinition> definitions = new TreeMap<>();
    private final Map<UUID, EnumMap<SlotType, List<String>>> slots = new HashMap<>();
    private final Map<UUID, Map<String, Long>> cooldowns = new HashMap<>();
    public AbilityService(Collection<AbilityDefinition> abilities) { for (var a:abilities) if(definitions.putIfAbsent(a.id(),a)!=null) throw new IllegalArgumentException("Duplicate ability: "+a.id()); }
    public Collection<AbilityDefinition> all() { return definitions.values(); }
    public void learn(PlayerRpgData player, String abilityId) {
        AbilityDefinition a=require(abilityId); if(player.learnedAbilities().contains(abilityId)) throw new IllegalStateException("Ability already learned"); JobProgress p=player.progressFor(a.jobId());
        if(p.level()<a.requiredLevel()) throw new IllegalStateException("Job level too low");
        if(!p.spendAbp(a.abpCost())) throw new IllegalStateException("Not enough ABP");
        player.learn(abilityId);
    }
    public void equip(PlayerRpgData player, String abilityId, SlotType type, int slot) {
        AbilityDefinition a=require(abilityId); if(!player.learnedAbilities().contains(abilityId)) throw new IllegalStateException("Ability not learned");
        if(a.passive() != (type==SlotType.PASSIVE)) throw new IllegalArgumentException("Wrong slot type");
        int max=type==SlotType.ACTIVE?4:2; if(slot<0||slot>=max) throw new IndexOutOfBoundsException("Slot out of range");
        EnumMap<SlotType,List<String>> map=slots.computeIfAbsent(player.playerId(),k->{var m=new EnumMap<SlotType,List<String>>(SlotType.class);m.put(SlotType.ACTIVE,new ArrayList<>(Collections.nCopies(4,null)));m.put(SlotType.PASSIVE,new ArrayList<>(Collections.nCopies(2,null)));return m;});
        map.get(type).set(slot,abilityId);
    }
    public CastResult cast(PlayerRpgData player,String abilityId,long gameTick) {
        AbilityDefinition a=require(abilityId); if(a.passive()) return new CastResult(false,"Passive ability cannot be cast");
        if(!player.learnedAbilities().contains(abilityId)) return new CastResult(false,"Ability not learned");
        if(!equipped(player.playerId(),abilityId,SlotType.ACTIVE)) return new CastResult(false,"Ability not equipped");
        if(!a.jobId().equals(player.activeJob())) return new CastResult(false,"Wrong active job");
        long ready=cooldowns.getOrDefault(player.playerId(),Map.of()).getOrDefault(abilityId,0L);
        if(gameTick<ready) return new CastResult(false,"Cooldown active");
        if(player.mana()<a.manaCost()) return new CastResult(false,"Insufficient mana");
        player.setMana(player.mana()-a.manaCost()); cooldowns.computeIfAbsent(player.playerId(),k->new HashMap<>()).put(abilityId,gameTick+a.cooldownTicks());
        return new CastResult(true,a.effect());
    }
    public boolean equipped(UUID player,String id,SlotType type) { var m=slots.get(player); return m!=null && m.get(type).contains(id); }
    private AbilityDefinition require(String id) { var a=definitions.get(id); if(a==null) throw new IllegalArgumentException("Unknown ability: "+id); return a; }
}
