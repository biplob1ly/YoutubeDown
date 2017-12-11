import com.sun.jndi.toolkit.url.Uri;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Created by Biplob on 6/24/2016.
 */
public class Main {

    public static void main(String[] args) throws UnsupportedEncodingException {
        int begin, end;
        String myurl = "";
        String tmpstr = null;
        try {
            URL url=new URL("https://www.youtube.com/watch?v=BPNTC7uZYrI");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            InputStream stream=con.getInputStream();
            InputStreamReader reader=new InputStreamReader(stream);
            StringBuffer buffer=new StringBuffer();
            char[] buf=new char[262144];
            int chars_read;
            while ((chars_read = reader.read(buf, 0, 262144)) != -1) {
                buffer.append(buf, 0, chars_read);
            }
            tmpstr=buffer.toString();

            begin  = tmpstr.indexOf("url_encoded_fmt_stream_map=");
            end = tmpstr.indexOf("&", begin + 27);
            if (end == -1) {
                end = tmpstr.indexOf("\"", begin + 27);
            }
            tmpstr = URLDecoder.decode(tmpstr.substring(begin + 27, end), "UTF-8");
            //System.out.println("here "+tmpstr);
        } catch (MalformedURLException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }

        Vector url_encoded_fmt_stream_map = new Vector();
        begin = 0;
        end   = tmpstr.indexOf(",");

        while (end != -1) {
            url_encoded_fmt_stream_map.addElement(tmpstr.substring(begin, end));
            begin = end + 1;
            end   = tmpstr.indexOf(",", begin);
        }

        url_encoded_fmt_stream_map.addElement(tmpstr.substring(begin, tmpstr.length()));
        String result = "";
        Enumeration url_encoded_fmt_stream_map_enum = url_encoded_fmt_stream_map.elements();
        while (url_encoded_fmt_stream_map_enum.hasMoreElements()) {
            tmpstr = (String)url_encoded_fmt_stream_map_enum.nextElement();
            System.out.println("here "+tmpstr);
            begin = tmpstr.indexOf("itag=");
            if (begin != -1) {
                end = tmpstr.indexOf("&", begin + 5);

                if (end == -1) {
                    end = tmpstr.length();
                }

                int fmt = Integer.parseInt(tmpstr.substring(begin + 5, end));
                //System.out.println("here "+fmt);
                if (fmt == 35) {
                    begin = tmpstr.indexOf("url=");
                    if (begin != -1) {
                        end = tmpstr.indexOf("&", begin + 4);
                        if (end == -1) {
                            end = tmpstr.length();
                        }
                        result = URLDecoder.decode((tmpstr.substring(begin + 4, end)), "UTF-8");
                        myurl=result;
                        //System.out.println("here "+myurl);
                        break;
                    }
                }
            }
        }

        URL u = null;
        InputStream is = null;
        myurl = "http://r3---sn-55hg5-q5jl.googlevideo.com/videoplayback?pcm2cms=yes&dur=297.029&mime=video%2Fmp4&itag=22&upn=B9AG2r7ryng&cnr=14&ipbits=0&signature=83949080059280727446CE5ECD416EB9675BB6F8.255AC501B950B00FC709F3878371ADEB56B64A12&sver=3&fexp=9405186%2C9416126%2C9416891%2C9422596%2C9428398%2C9431012%2C9433096%2C9433221%2C9433720%2C9433946%2C9435526%2C9435876%2C9436805%2C9436986%2C9437066%2C9437088%2C9437283%2C9437553%2C9437988%2C9438283%2C9438338%2C9438361%2C9438663%2C9439433%2C9439640%2C9439652%2C9439779%2C9439813%2C9440049&key=yt6&initcwndbps=576250&lmt=1394359151321603&expire=1466788365&sparams=cnr%2Cdur%2Cid%2Cinitcwndbps%2Cip%2Cipbits%2Citag%2Clmt%2Cmime%2Cmm%2Cmn%2Cms%2Cmv%2Cpcm2cms%2Cpl%2Cratebypass%2Csource%2Cupn%2Cexpire&source=youtube&mm=31&mn=sn-55hg5-q5jl&ip=203.202.252.68&ratebypass=yes&mt=1466766452&mv=m&pl=24&id=o-ABKb5bC7dtwf4rMX9rsH33iRPbW5gq2dEAGoCdGJeDZB&ms=au&title=1uP%20-%20WHAT%3F";
        try {
            u = new URL(myurl);
            is = u.openStream();
            HttpURLConnection huc = (HttpURLConnection)u.openConnection(); //to know the size of video
            int size = huc.getContentLength();

            if(huc != null) {
                String fileName = "FILE.mp4";
                //String storagePath = Environment.getExternalStorageDirectory().toString();
                //File f = new File(storagePath,fileName);
                File f = new File(fileName);
                FileOutputStream fos = new FileOutputStream(f);
                byte[] buffer = new byte[1024];
                int len1 = 0;
                if(is != null) {
                    while ((len1 = is.read(buffer)) > 0) {
                        fos.write(buffer,0, len1);
                    }
                }
                if(fos != null) {
                    fos.close();
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                // just going to ignore this one
            }
        }
    }
}
