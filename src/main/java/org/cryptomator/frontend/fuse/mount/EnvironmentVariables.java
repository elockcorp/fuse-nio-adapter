package org.cryptomator.frontend.fuse.mount;

import java.nio.file.Path;
import java.util.Optional;

public class EnvironmentVariables {

	private final Path mountPath;
	private final Optional<String> mountName;
	private final Optional<String> revealCommand;
	/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX195veGzocK2MO1Gy2wDFGklTnx8zoNHQ6XxNpmA9YAa9NLXyLjS4iIR
yoqGrtxioXwR2eockrp5nw==
	###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
	private final Optional<String> volIconPath;

	/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX191Og9HxbSQc4FN7qgAd1OGNuNbOLLUBNd5NDtty5fGmQHATr+tR8K+
uMQ8llMlDaTgyAi3swCP/5qXgH6P+adJgyG9rBiq0k4=
	###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
	private EnvironmentVariables(Path mountPath, Optional<String> mountName, Optional<String> revealCommand, Optional<String> volIconPath) {
		this.mountPath = mountPath;
		this.mountName = mountName;
		this.revealCommand = revealCommand;
		this.volIconPath = volIconPath;
	}

	public static EnvironmentVariablesBuilder create() {
		return new EnvironmentVariablesBuilder();
	}

	public Path getMountPath() {
		return mountPath;
	}

	public Optional<String> getMountName() {
		return mountName;
	}

	public Optional<String> getRevealCommand() {
		return revealCommand;
	}

	/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1+cbI3JkAA26fTGH7nzQRPjwi/iEOgV46rFBf5IEd//b+XmPbpKESuO
bPxW37LzAF9Zoby/jvXXHlqdsL6DEq2bPwcvjucoECU=
	###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
	public Optional<String> getVolumeIconPath() {
		return volIconPath;
	}

	public static class EnvironmentVariablesBuilder {

		private Path mountPath = null;
		private Optional<String> mountName = Optional.empty();
		private Optional<String> revealCommand = Optional.empty();
		/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1+musoXRKzZcOFtkZQ38smBX6tZB12zOfk0R120lYFFOcsVO8XH0l7v
VWCUk9izfjf6ujyDG3gB9rlId+YWab/CzVys3cafvUw=
		###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
		private Optional<String> volIconPath = Optional.empty();

		public EnvironmentVariablesBuilder withMountPath(Path mountPath) {
			this.mountPath = mountPath;
			return this;
		}

		public EnvironmentVariablesBuilder withMountName(String mountName) {
			this.mountName = Optional.ofNullable(mountName);
			return this;
		}

		public EnvironmentVariablesBuilder withRevealCommand(String revealCommand) {
			this.revealCommand = Optional.ofNullable(revealCommand);
			return this;
		}

		/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1/GLZvpH+HRBsfSqvQh358pxOnZyy+KlWR99DMEDpzuLud8MvCRK2dG
b+vMzYOoJ2FouMpIAW3Rvp92Q7YTT9i3r1Ky/2x2alE=
		###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
		public EnvironmentVariablesBuilder withVolIconPath(String volIconPath) {
			this.volIconPath = Optional.ofNullable(volIconPath);
			return this;
		}

		public EnvironmentVariables build() {
			/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1+XU4IJIZj6Z9dxz0hNL64vXhPkBDPPipwuoxG251pmhzcG0IsqDaAY
NUB5XuOyP6NWshSaMx8nv2DBNlqFIXc1yCSPU2BLIqs=
			###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
			return new EnvironmentVariables(mountPath, mountName, revealCommand, volIconPath);
		}

	}
}
