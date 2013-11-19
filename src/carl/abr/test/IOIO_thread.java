/*******************************************************************************************************
Copyright (c) 2011 Regents of the University of California.
All rights reserved.

This software was developed at the University of California, Irvine.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions
are met:

1. Redistributions of source code must retain the above copyright
   notice, this list of conditions and the following disclaimer.

2. Redistributions in binary form must reproduce the above copyright
   notice, this list of conditions and the following disclaimer in
   the documentation and/or other materials provided with the
   distribution.

3. All advertising materials mentioning features or use of this
   software must display the following acknowledgment:
   "This product includes software developed at the University of
   California, Irvine by Nicolas Oros, Ph.D.
   (http://www.cogsci.uci.edu/~noros/)."

4. The name of the University may not be used to endorse or promote
   products derived from this software without specific prior written
   permission.

5. Redistributions of any form whatsoever must retain the following
   acknowledgment:
   "This product includes software developed at the University of
   California, Irvine by Nicolas Oros, Ph.D.
   (http://www.cogsci.uci.edu/~noros/)."

THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
IN NO EVENT SHALL THE UNIVERSITY OR THE PROGRAM CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *******************************************************************************************************/

package carl.abr.test;

import ioio.lib.api.AnalogInput;
import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.PwmOutput;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;

public class IOIO_thread extends BaseIOIOLooper 
{
	private AnalogInput input_;
	private PwmOutput pwmOutput_;
	private DigitalOutput led_;
	Main_activity the_gui;					// reference to the main activity

	public IOIO_thread(Main_activity gui)
	{
		the_gui = gui;
	}

	@Override
	public void setup() throws ConnectionLostException 
	{
		try {
			input_ = ioio_.openAnalogInput(40);
			pwmOutput_ = ioio_.openPwmOutput(7, 100);
			led_ = ioio_.openDigitalOutput(IOIO.LED_PIN, true);

			the_gui.enableUi(true);
		} catch (ConnectionLostException e) 
		{
			the_gui.enableUi(false);
			throw e;
		}
	}

	@Override
	public void loop() throws ConnectionLostException 
	{
		try 
		{
			final float reading = input_.read();
			the_gui.setText(Float.toString(reading));

			pwmOutput_.setPulseWidth(500 + the_gui.seekBar_.getProgress() * 2);
			led_.write(!the_gui.toggleButton_.isChecked());

			Thread.sleep(10);
		} catch (InterruptedException e) {
			ioio_.disconnect();
		} catch (ConnectionLostException e) {
			the_gui.enableUi(false);
			throw e;
		}
	}
}
