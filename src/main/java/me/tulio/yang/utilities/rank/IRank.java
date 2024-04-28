// Decompiled with: Procyon 0.6.0
// Class Version: 8
package me.tulio.yang.utilities.rank;

import java.util.UUID;

public interface IRank
{
    String getRankSystem();
    
    String getName(final UUID p0);
    
    String getPrefix(final UUID p0);
    
    String getSuffix(final UUID p0);
    
    String getColor(final UUID p0);
    
    int getWeight(final UUID p0);
}
