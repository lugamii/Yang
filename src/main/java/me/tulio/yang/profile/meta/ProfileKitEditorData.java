package me.tulio.yang.profile.meta;

import lombok.Getter;
import lombok.Setter;
import me.tulio.yang.kit.Kit;
import me.tulio.yang.kit.KitLoadout;

public class ProfileKitEditorData {

	@Getter @Setter private boolean active;
	@Setter private boolean rename;
	@Getter @Setter private Kit selectedKit;
	@Getter @Setter private KitLoadout selectedKitLoadout;

	public boolean isRenaming() {
		return this.active && this.rename && this.selectedKit != null;
	}

}
