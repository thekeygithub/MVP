using Newtonsoft.Json;
using Newtonsoft.Json.Converters;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Runtime.Serialization.Json;
using System.Text;
using System.Threading.Tasks;

namespace operateProxy
{
    public class Servlet
    {
        public virtual void onGet(System.Net.HttpListenerRequest request, System.Net.HttpListenerResponse response) { }
        public virtual void onPost(System.Net.HttpListenerRequest request, System.Net.HttpListenerResponse response) { }
        public virtual void onCreate()
        {

        }
    }
    public class MyServlet : Servlet
    {
        static int index = 0;

        public override void onCreate()
        {
            base.onCreate();
        }

        public override void onGet(HttpListenerRequest request, HttpListenerResponse response)
        {
            Console.WriteLine("GET:" + request.Url);
            byte[] buffer = Encoding.UTF8.GetBytes("OK");

            System.IO.Stream output = response.OutputStream;
            output.Write(buffer, 0, buffer.Length);
            // You must close the output stream.
            output.Close();
            //listener.Stop();
        }

        public override void onPost(HttpListenerRequest request, HttpListenerResponse response)
        {
        //    Console.WriteLine("POST:" + request.Url);
            System.IO.Stream input = request.InputStream;
            StreamReader reader = new StreamReader(input);
            string text = reader.ReadToEnd();
             string name = Dns.GetHostName();
            IPAddress[] ipadrlist = Dns.GetHostAddresses(name);
             foreach (IPAddress ipa in ipadrlist)
               {
                if (ipa.AddressFamily == AddressFamily.InterNetwork)
                Console.WriteLine(ipa.ToString());
            }
         //   Console.WriteLine("POST:" + text);

            //    ShowMenu();
            Dictionary<string, string> paramMap = new Dictionary<string, string>();
            List<string> args = new List<string>();
            string[] sArray = text.Split(',');
            if (sArray.Length < 2) {
                byte[] error = Encoding.UTF8.GetBytes("The request param is error");
                response.OutputStream.Write(error, 0, error.Length);
                return;
            }
            for (int i = 0; i < sArray.Length; i = i + 1)
            {
                string vstr = sArray[i];
                string[] sArray2 = vstr.Split(':');
                if (sArray2.Length < 2)
                {
                    byte[] error = Encoding.UTF8.GetBytes("The request param is error");
                    response.OutputStream.Write(error, 0, error.Length);
                    return;
                }
                string key = sArray2[0];
                string value = sArray2[1];
                paramMap.Add(key, value);
            }
            Program.InitTest();
            string sciptHash = "";
            string privateKey = "";
            string[] privateKeyList = new string[10];
            //save
            privateKeyList[0] = "KysSjeqG1Q8AXWxJGKFKzMvdAkjubLBw8MS3kCAcoto89DAY4GYu";
            privateKeyList[1] = "L2ft1qgpRjXz9nhwU8WuVTF1Tyw1pouiZ7hfZKoJnAXUvpCuBf52";
            privateKeyList[2] = "L2MQNocjDTjvwL4Rrqky2x3ChHemFfmQZV75JDQAdG3TgHbvUG54";
            privateKeyList[3] = "L1Kw282UsoBHd2EKMukRbT9sBgtbQD3kDaRYEde58mf6P7vZq1Am";
            privateKeyList[4] = "L5RzNL3Q6tyA4SaTV9NGwS5tdL5PT7aLr6jSysLsZDfbGbdME4GL";
            privateKeyList[5] = "KzpTsKdFGEmiit5hucYaLqgB4GBSncka7pLnWXf97uHG7dy3u6qE";
            privateKeyList[6] = "KyTd2ZjYAyNcxp7G8fkG9rsRTbbFFRZs5Qdg2UaKPt1UJuLbd8xz";
            privateKeyList[7] = "KxAdE9JfBMqniSrojH5qS9do5dN1zCuzWWZZCPojkCXgParBsZW3";
            privateKeyList[8] = "KxQwPBz48sQCtMQyNQBbhNT3qE5bEb8RpYiuTfRkCPCvWVViwpac";
            privateKeyList[9] = "L1JW3tcxsvtEHaeEeimBub3SBPcR67zddSpHjFfamSXPSi3Na6yP";
            //saveResult
            //privateKeyList[0] = "L1Xi7efjAhWgnxwhBXfVEG5ycDHw2WBFuVJGXjDMHEbBHERNiLdP";
            //privateKeyList[1] = "L3aY6WJuG7cz8fYmGESqwEJb29rZq28av9PGuzrDyv2E2Yrw24cz";
            //privateKeyList[2] = "L24vd3zvkBofBP1TxGokBFrRUfR5DJYXGRejpCEZS6ZeaqZNnNAX";
            //privateKeyList[3] = "L4Vfp66ey99nmnzNw2mejNDmaFkpPsnx7ruCYWjzoG8mb5jCky6p";
            //privateKeyList[4] = "L5CZcfxYGXNUy6Rf8z7A8abQq2gS3gX41BVSqGnNjA33rSuuw9aU";
            //privateKeyList[5] = "L2LcvYcpjLA4hATpxggCWdKhsRJgibbBUqNY7arVrdYNskSLZY6X";
            //privateKeyList[6] = "KzmuqKiDudWkGzS7cYXPRBdLgZanAQidBFDKorikJTqL2CrM9bes";
            //privateKeyList[7] = "L26uPRMX8TvT4iHXS65odaoPyNCJiwTBxEhuL4qwT6ThE6tektVv";
            //privateKeyList[8] = "L5hq7ensiFvFKcViQXbdxtoeaCoY2sBjUq1QrAy6MueR3KyDChge";
            //privateKeyList[9] = "L1fSvXx8tyGvgXxJEYeLNxTBEAYoJtHVSwLAqL9jqSS1QofvZN5Z";
            privateKey = privateKeyList[index];
            if (index == 9)
                index = 0;
            else
                index = index + 1;
            if (paramMap.ContainsKey("method") &&(paramMap["method"].Equals("saveValidateInfo")||paramMap["method"].Equals("savePayInfo")))
            {
                sciptHash = "0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";
                if (paramMap.ContainsKey("value"))
                    args.Add(paramMap["value"]);
                else
                {
                    byte[] error = Encoding.UTF8.GetBytes("The request param is error");
                    response.OutputStream.Write(error, 0, error.Length);
                    return;
                }
            }
            else if (paramMap.ContainsKey("method") && paramMap["method"].Equals("saveResult"))
            {
                sciptHash = "0x6bd3f062a37ac2b19d6f06a819f9b1f2464bbca4";
                if (paramMap.ContainsKey("key") && paramMap.ContainsKey("value")) {
                    args.Add(paramMap["key"]);
                    args.Add(paramMap["value"]);
                }else
                {
                    byte[] error = Encoding.UTF8.GetBytes("The request param is error");
                    response.OutputStream.Write(error, 0, error.Length);
                    return;
                }
            }
            else if (paramMap.ContainsKey("method") && paramMap["method"].Equals("transfer"))
            {
                sciptHash = "0x342c8b1242c195929b109079da947b1e973fe2be";
                args.Add(paramMap["from"]);
                args.Add(paramMap["to"]);
                args.Add(paramMap["value"]);
            }
            else if (paramMap.ContainsKey("method") && paramMap["method"].Equals("allotTKY"))
            {
                privateKey = "L3dGB3mtFcySiPWQxGbpoib2toPXhK2KfQqtHhZjnW151nUqfjVG";
                sciptHash = "0xcf5b19f1fba56d890742840b29b5203b33faedfc";
                args.Add(paramMap["yb"]);
                args.Add(paramMap["yys"]);
            }
            else
            {
            }
            Program.InitTest();
            //      Task<string> result = Program.AsyncLoop("sc3", "0x55b300f8468d9e2bb9c3a4b8ff870c8c3b6ddf7c", "Kwg7Sps8VuZqbRaJXXsg1HdqMWoE6MeUBV7epbW5w1maiL49x7x7", "AMZiiAnBg5uFLXGVMSt4hPEA2ubRCiNZg5", paramMap["method"], args);
            Task<string> result = Program.AsyncLoop("sc3", sciptHash, privateKey, paramMap["method"], args);
            string text2 = result.Result;
         //   result.ContinueWith(TaskEnd);
            byte[] res = Encoding.UTF8.GetBytes(text2);
            response.OutputStream.Write(res, 0, res.Length);
        }
        void TaskEnd(System.Threading.Tasks.Task<string> task)
        {
            // StringBuilder sb = new StringBuilder();
            //sb.AppendLine("任务已经完成...");
            //sb.AppendLine("是否因为被取消而完成:" + task.IsCanceled.ToString());
            //sb.AppendLine("成功完成:" + task.IsCompleted.ToString());
            //sb.AppendLine("是否因为发生异常而完成:" + task.IsFaulted.ToString());
            //  sb.AppendLine(task.Result);
            while (true)
            {
                System.Threading.Thread.Sleep(100);
            }

        }

        public static string ObjectToJSON<T>(T obj)
        {
            // Framework 2.0 不支持  
            DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
            string result = string.Empty;
            using (MemoryStream ms = new MemoryStream())
            {
                serializer.WriteObject(ms, obj);
                ms.Position = 0;

                using (StreamReader read = new StreamReader(ms))
                {
                    result = read.ReadToEnd();
                }
            }
            return result;
        }
    }

}
