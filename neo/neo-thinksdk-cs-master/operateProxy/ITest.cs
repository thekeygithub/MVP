using System;
using System.Collections.Generic;
using System.Threading.Tasks;

namespace operateProxy
{
    interface OperateType
    {
        string Name
        {
            get;
        }
        string ID
        {
            get;
        }
        Task<string> Operation(string script, string prikeyStr,string method, List<string> args);
    }
    public class Program
    {
        static Dictionary<string, OperateType> alltest = new System.Collections.Generic.Dictionary<string, OperateType>();
        static void RegTest(OperateType test)
        {
            alltest[test.ID.ToLower()] = test;
        }
        public static void InitTest()
        {
            // RegTest(new Height());
            //RegTest(new SCDemo1());
            //RegTest(new SCDemo2());
            RegTest(new SCDUtil());
        }
        public async static Task<string> AsyncLoop(string line, string script, string prikeyStr, string method, List<string> args)
        {
            var test = alltest[line];
            //Console.WriteLine("[begin]" + test.Name);
           // Console.WriteLine("[end]" + test.Name);
            return await test.Operation(script, prikeyStr,method, args);
        }

    }

}
