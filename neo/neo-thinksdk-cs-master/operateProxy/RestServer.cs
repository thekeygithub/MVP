using System;
using System;
using System.Net;
using System.Threading;

namespace operateProxy
{
    class RestServer
    {
        static void Main(string[] args)
        {
            HttpListener httpListenner = new HttpListener();
            httpListenner.AuthenticationSchemes = AuthenticationSchemes.Anonymous;
            httpListenner.Prefixes.Add("http://+:9000/");
            httpListenner.Start();

            new Thread(new ThreadStart(delegate
            {
                try
                {
                    loop(httpListenner);
                }
                catch (Exception)
                {
                    httpListenner.Stop();
                }
            })).Start();

            Console.WriteLine("9000端口启动成功！");
            Console.ReadKey();
        }

        /**
         * 
         * 监听
         * **/
        private static void loop(HttpListener httpListenner)
        {
            while (true)
            {
                HttpListenerContext context = httpListenner.GetContext();
                HttpListenerRequest request = context.Request;
                HttpListenerResponse response = context.Response;
                Servlet servlet = new MyServlet();
                servlet.onCreate();
                if (request.HttpMethod == "POST")
                {
                    servlet.onPost(request, response);
                }
                else if (request.HttpMethod == "GET")
                {
                    servlet.onGet(request, response);
                }
                response.Close();
            }
        }

    }

}
