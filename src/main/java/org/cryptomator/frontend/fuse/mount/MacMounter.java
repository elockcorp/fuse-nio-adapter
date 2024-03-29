package org.cryptomator.frontend.fuse.mount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

class MacMounter implements Mounter {

	private static final Logger LOG = LoggerFactory.getLogger(MacMounter.class);
	private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");
	private static final int[] OSXFUSE_MINIMUM_SUPPORTED_VERSION = new int[]{3, 8, 2};
	private static final Path OSXFUSE_VERSIONFILE_LOCATION = Paths.get("/Library/Filesystems/osxfuse.fs/Contents/version.plist");
	private static final String OSXFUSE_XML_VERSION_TEXT = "CFBundleShortVersionString";

	@Override
	public Mount mount(Path directory, EnvironmentVariables envVars, String... additionalMountParams) throws CommandFailedException {
		MacMount mount = new MacMount(directory, envVars);
		mount.mount(additionalMountParams);
		return mount;
	}

	/**
	 * @return <code>true</code> if on OS X and osxfuse with a higher version than the minimum supported one is installed.
	 */
	@Override
	public boolean isApplicable() {
		return IS_MAC && Files.exists(Paths.get("/usr/local/lib/libosxfuse.2.dylib")) && installedVersionSupported();
	}

	public boolean installedVersionSupported() {
		String versionString = getVersionString();
		if (versionString == null) {
			LOG.error("Did not find {} in document {}.", OSXFUSE_XML_VERSION_TEXT, OSXFUSE_VERSIONFILE_LOCATION);
			return false;
		}

		Integer[] parsedVersion = Arrays.stream(versionString.split("\\.")).map(s -> Integer.valueOf(s)).toArray(Integer[]::new);
		for (int i = 0; i < OSXFUSE_MINIMUM_SUPPORTED_VERSION.length && i < parsedVersion.length; i++) {
			if (parsedVersion[i] < OSXFUSE_MINIMUM_SUPPORTED_VERSION[i]) {
				return false;
			} else if (parsedVersion[i] > OSXFUSE_MINIMUM_SUPPORTED_VERSION[i]) {
				return true;
			}
		}

		if (OSXFUSE_MINIMUM_SUPPORTED_VERSION.length <= parsedVersion.length) {
			return true;
		} else {
			return false;
		}
	}


	private String getVersionString() {
		String version = null;
		try (InputStream in = Files.newInputStream(OSXFUSE_VERSIONFILE_LOCATION, StandardOpenOption.READ)) {
			XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(in);
			while (reader.hasNext()) {
				reader.next();
				if (reader.getEventType() == XMLStreamReader.CHARACTERS && OSXFUSE_XML_VERSION_TEXT.equalsIgnoreCase(reader.getText())) {
					reader.next();
					reader.next();
					reader.next();
					version = reader.getElementText();
				}
			}
		} catch (XMLStreamException | FactoryConfigurationError e) {
			LOG.error("Could not parse file {} to detect version of OSXFUSE.", OSXFUSE_VERSIONFILE_LOCATION);
		} catch (IOException e1) {
			LOG.error("Could not read file {} to detect version of OSXFUSE.", OSXFUSE_VERSIONFILE_LOCATION);
		}
		return version;
	}

	private static class MacMount extends AbstractMount {

		private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

		private final ProcessBuilder revealCommand;
		private final ProcessBuilder unmountCommand;
		private final ProcessBuilder unmountForcedCommand;

		private MacMount(Path directory, EnvironmentVariables envVars) {
			super(directory, envVars);
			Path mountPoint = envVars.getMountPath();
			this.revealCommand = new ProcessBuilder("open", ".");
			this.revealCommand.directory(mountPoint.toFile());
			this.unmountCommand = new ProcessBuilder("umount", mountPoint.getFileName().toString());
			this.unmountCommand.directory(mountPoint.getParent().toFile());
			this.unmountForcedCommand = new ProcessBuilder("umount", "-f", mountPoint.getFileName().toString());
			this.unmountForcedCommand.directory(mountPoint.getParent().toFile());
		}

		@Override
		protected String[] getFuseOptions() {
			// see: https://github.com/osxfuse/osxfuse/wiki/Mount-options
			ArrayList<String> mountOptions = new ArrayList<>();
			try {
				mountOptions.add("-ouid=" + Files.getAttribute(USER_HOME, "unix:uid"));
				mountOptions.add("-ogid=" + Files.getAttribute(USER_HOME, "unix:gid"));
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
			mountOptions.add("-oatomic_o_trunc");
			mountOptions.add("-ovolname=" + envVars.getMountName().orElse("vault"));
			/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1/kRoCkc5N90+NHcHXYMQAyqTu8xcerIGAOGpz+YBB0x7sR4GTjy6M/
Q8Q/2NBt3BdLpDj6wJBweBanLg7aGLw8jYQKfxPkUcwXqMpLDxDsw3U35locF5am
q8pLN4knYIsePQUTUEVm0g==
			###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
			//mountOptions.add("-oauto_xattr"); // TESTING
			mountOptions.add("-oauto_cache");
			/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1/1+clMGCthcw8aq9tgHd4dva/IuDp0PFBPAOmHN1VjNJttR9slWPnT
b/S0wjKfhh9HseDShi/rhj9OaVMvrTlh4sDL/LFWQoEITc2vO3cJyME5odzcx6CX
8u+2yyj4BZu8aa/IpuH8RMWYxjPKI3Up0oXQH94qjeryibJouWUtPUnsd8NXcR9J
t4zDICA01Q6yP8zOSAALIN8uRO+rSDpK58uQUG+RiNMdYrkFCR+9ihk2QirH8zJg
5HnztwoOlLgwr34ShyHPb5EGMk8Mmt7c7c9JISNHWjyDXuy5A+5Bt0WSKRpO9jNe
VOCBkMDehiSxO7evPK2PlLLEJi0SVSad+OQ0CVz/RXCZNCzqwdYTwYBtgQk18Vwc
			###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
			mountOptions.add("-onoappledouble"); // vastly impacts performance for some reason...
			mountOptions.add("-odefault_permissions"); // let the kernel assume permissions based on file attributes etc
			/* ###_VIRTUALSAFE_CHANGE_TRACKING_START_###
U2FsdGVkX1/gRGz36SRHGgaeynZU20i1jt53Obhk/ZN2vCdoV8Asl0AMN0QHP/Is
VPssoICNrB4ZpLh1dG81j2JxaE3rxKHSgV5QT5pFhpFKn7DXuzNpWbV+dJKl9DZO
MTZR2Ia84tXWeZGjThaEeTP7NUaDjdBF+wYYP8GhEBN3NHxGaXFEHAxo6h/PLHzf
AzTDbNmOMLx/hiGEu99sOhiHhodddyuER96G3wifFD3e82sX6orOQKxFa/hhSHWi
BpmEqMf4P/09BdW+3iYOqqvsmyQgt9EQvTnO/O8uUYIiNhlRO2gsGycuh02yZqjS
iuN3wgl8El4oIU34W//W4FZhw2zmesaxJCoFeIeh132FjgXmPfnQQoFqURzrOyjJ
KHiCTKhvBO9e5pQNt0ymlqjRZ83KIt+fi6doYkATb6C8cDz9CPNncryB9S22/FEy
			###_VIRTUALSAFE_CHANGE_TRACKING_END_### */
			if (envVars.getVolumeIconPath().isPresent()) {
				mountOptions.add("-omodules=iconv:volicon");
				mountOptions.add("-oiconpath=" + envVars.getVolumeIconPath().get());
				mountOptions.add("-ofrom_code=UTF-8");
				mountOptions.add("-oto_code=UTF-8-MAC");
			}
			else {
				mountOptions.add("-omodules=iconv,from_code=UTF-8,to_code=UTF-8-MAC"); // show files names in Unicode NFD encoding
			}
			return mountOptions.toArray(new String[mountOptions.size()]);
		}

		@Override
		public ProcessBuilder getRevealCommand() {
			return revealCommand;
		}

		@Override
		public ProcessBuilder getUnmountCommand() {
			return unmountCommand;
		}

		@Override
		public ProcessBuilder getUnmountForcedCommand() {
			return unmountForcedCommand;
		}

	}

}
