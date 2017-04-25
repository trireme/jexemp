import java.lang.management.ManagementFactory;
import java.util.concurrent.TimeUnit;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.OS;
import org.apache.commons.exec.PumpStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemInfo {
	private static final Logger LOG = LoggerFactory.getLogger(SystemInfo.class);

	public static boolean isProcessRunning(int pid, int timeout, TimeUnit timeunit) throws java.io.IOException {
		String line;
		if (OS.isFamilyWindows()) {
			// tasklist exit code is always 0. Parse output
			// findstr exit code 0 if found pid, 1 if it doesn't
			line = "cmd /c \"tasklist /FI \"PID eq " + pid + "\" | findstr " + pid + "\"";
		} else {
			// ps exit code 0 if process exists, 1 if it doesn't
			line = "ps -p " + pid;
			// `-p` is POSIX/BSD-compliant, `--pid` isn't<ref>https://github.com/apache/storm/pull/296#discussion_r20535744</ref>
		}
		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		// disable logging of stdout/strderr
		executor.setStreamHandler(new PumpStreamHandler(null, null, null));
		// disable exception for valid exit values
		executor.setExitValues(new int[] { 0, 1 });
		// set timer for zombie process
		ExecuteWatchdog timeoutWatchdog = new ExecuteWatchdog(timeunit.toMillis(timeout));
		executor.setWatchdog(timeoutWatchdog);
		int exitValue = executor.execute(cmdLine);
		// 0 is the default exit code which means the process exists
		return exitValue == 0;
	}

	/**
	 * Try to get current PID, usage: getProcessId("<PID>"), may fail in some JVM implementations, therefore fallback has to be provided
	 * 
	 * @param fallback
	 *            - for example "<PID>"
	 * @return
	 */
	public static String getProcessId(final String fallback) {

		// something like '<pid>@<hostname>', at least in SUN / Oracle JVMs
		final String jvmName = ManagementFactory.getRuntimeMXBean().getName();
		final int index = jvmName.indexOf('@');

		if (index < 1) {
			// part before '@' empty (index = 0) / '@' not found (index = -1)
			return fallback;
		}

		try {
			return Long.toString(Long.parseLong(jvmName.substring(0, index)));
		} catch (NumberFormatException e) {
			LOG.warn("PID konnte nicht gelesen werden");
		}
		return fallback;
	}
}
