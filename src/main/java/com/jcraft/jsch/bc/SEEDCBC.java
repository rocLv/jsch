/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/*
Copyright (c) 2005-2018 ymnk, JCraft,Inc. All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

  1. Redistributions of source code must retain the above copyright notice,
     this list of conditions and the following disclaimer.

  2. Redistributions in binary form must reproduce the above copyright 
     notice, this list of conditions and the following disclaimer in 
     the documentation and/or other materials provided with the distribution.

  3. The names of the authors may not be used to endorse or promote products
     derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JCRAFT,
INC. OR ANY CONTRIBUTORS TO THIS SOFTWARE BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.jcraft.jsch.bc;

import com.jcraft.jsch.Cipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.SEEDEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.*;

public class SEEDCBC implements Cipher{
  private static final int ivsize=16;
  private static final int bsize=16;
  private BufferedBlockCipher cipher;
  @Override
  public int getIVSize(){return ivsize;}
  @Override
  public int getBlockSize(){return bsize;}
  @Override
  public void init(int mode, byte[] key, byte[] iv) throws Exception{
    byte[] tmp;
    if(iv.length>ivsize){
      tmp=new byte[ivsize];
      System.arraycopy(iv, 0, tmp, 0, tmp.length);
      iv=tmp;
    }
    if(key.length>bsize){
      tmp=new byte[bsize];
      System.arraycopy(key, 0, tmp, 0, tmp.length);
      key=tmp;
    }

    try{
      ParametersWithIV keyspec=new ParametersWithIV(new KeyParameter(key, 0, key.length), iv, 0, iv.length);
      cipher=new BufferedBlockCipher(new CBCBlockCipher(new SEEDEngine()));
      cipher.init(mode==ENCRYPT_MODE, keyspec);
    }
    catch(Exception e){
      cipher=null;
      throw e;
    }
  }
  @Override
  public void update(byte[] foo, int s1, int len, byte[] bar, int s2) throws Exception{
    cipher.processBytes(foo, s1, len, bar, s2);
  }
  @Override
  public boolean isCBC(){return true; }
}
