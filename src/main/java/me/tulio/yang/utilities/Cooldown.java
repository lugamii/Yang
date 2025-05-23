package me.tulio.yang.utilities;

import lombok.Data;
import lombok.Setter;
import me.tulio.yang.utilities.string.TimeUtil;

@Data
public class Cooldown {

	private long start = System.currentTimeMillis();
	private long expire;
	@Setter boolean forceExpired;
	private boolean notified;

	public Cooldown(long duration) {
		this.expire = this.start + duration;

		if (duration == 0) {
			this.notified = true;
		}
	}

	public long getPassed() {
		return System.currentTimeMillis() - this.start;
	}

	public long getRemaining() {
		return this.expire - System.currentTimeMillis();
	}

	public boolean hasExpired() {
		return System.currentTimeMillis() - this.expire >= 0;
	}

	public boolean isForceExpired() {
		return this.forceExpired;
	}

	public String getTimeLeft() {
		if (this.getRemaining() >= 60_000) {
			return TimeUtil.millisToRoundedTime(this.getRemaining());
		} else {
			return TimeUtil.millisToSeconds(this.getRemaining());
		}
	}

}
