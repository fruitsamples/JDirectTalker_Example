/** * File:		MRJTalker.java * *	Contains:	Implementations of a simple Speech Manager class for Java *					using the MRJ JDirect Implementation.  Requires the Speech *					Manager extension to be present to run. * *	Version:	1.5 * *	Technology:	System 7.6.1 or later *	 *	Package:	Mac OS Runtime For Java� SDK 2.0.1 * *	Copyright:	� 1984-1998 by Apple Computer, Inc. *					All rights reserved. * *	Bugs?:		If you find a problem with this file, report them to mrj_bugs. *					Include the file and version information (from above) *					in the problem description and send to: *					Internet:	mrj_bugs@apple.com *//** * This class is a re-implementation of the NativeLibrary example, MRJTalker. * It uses Apple's JDirect technology to access the speech library directly. */import  com.apple.jdirect.SharedLibrary;import  com.apple.mrj.jdirect.JDirectLinker;public interface MRJTalkerLibrary extends SharedLibrary {	static Object libraryInstance = JDirectLinker.loadLibrary("SpeechLib");}public class MRJTalker implements MRJTalkerLibrary {		/**	 * We don't allow anyone to instantiate us	 */		protected MRJTalker() {	}		/**	 * Because the default speech channel is a system resource	 * and we must limit access to it, we act as a "factory" object	 * and only ever return one instance of it.	 *	 * Note that loadLibrary is case sensitive, and the name	 * of the code fragment must match precisely - we use the	 * CFM to resolve these names, as described in	 * "Inside Macintosh: PowerPC System Software"	 */	static MRJTalker theTalker = null;		public static MRJTalker getTalker() {		if (theTalker == null) {			theTalker = new MRJTalker();		}		return theTalker;	}		/**	 * The method that does the talking is synchronized	 * so that only one person can say something at a time.	 */	public synchronized void speakString(String s) {		// turn the string into a pascal string array of bytes		byte b[] = toStr255(s);				// call the native method		SpeakString(b);				// block until the speech is done		while (SpeechBusy()) {			try { 				Thread.currentThread().sleep(250);			} catch (InterruptedException e) {			}		}	}		/** converts a Java string to a Pascal-style string. */	private static byte[] toStr255(String str) {		int length = str.length();		if (length > 255) length = 255;		byte result[] = new byte[length + 1];		result[0] = (byte)length;		str.getBytes(0, length, result, 1);		return result;	}		/**	 * The JDirect implementation	 */	/** Declare the native methods that we're accessing */	private native static void SpeakString(byte b[]);	private native static boolean SpeechBusy();};