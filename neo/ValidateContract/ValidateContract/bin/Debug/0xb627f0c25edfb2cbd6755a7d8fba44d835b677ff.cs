using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Services.Neo;
using System;
using System.Numerics;

namespace ValidateContract
{
    public class Contract1 : SmartContract
    {
        public static readonly byte[] Owner = "AMZiiAnBg5uFLXGVMSt4hPEA2ubRCiNZg5".ToScriptHash();

        public static object Main(string operation, object[] args)
        {

           if (Runtime.Trigger == TriggerType.Verification)
            {
                return Runtime.CheckWitness(Owner);
            }
            else if (Runtime.Trigger == TriggerType.Application)
           {
                if (operation == "save") return saveInfo((string)args[0],(string)args[1]);
                if (operation == "get") return  Helper.AsString(getInfo((string)args[0]));
                if (operation == "saveResult") return saveResult((string)args[0],(string)args[1]);
                if (operation == "getRule") return getRule((string)args[0]);
            }
            return "fault";
        }
        /**
         * 查询信息
         * */
        public static byte[] getInfo(string key)
        {
            return Storage.Get(Storage.CurrentContext, key);
        }
        /**
         * 存储信息，返回获取数据的key
         * **/
        public static string saveInfo(string key,string value)
        {
            Storage.Put(Storage.CurrentContext, key, value);
            return key;
        }

        public static string saveResult(string key2, string value)
        {
            Storage.Put(Storage.CurrentContext, key2+"rs", value);
            return  key2+"rs";
        }

        public static string getRule(string key2)
        {
            if ("01".Equals(key2))
                return "{\"NeedInfo\":\"A1,A2,A3,A4,A5,H1,H2,H3,H4,H4,H6\",\"InterfaceOrder\":\"SBKJBXX,SBKTXXX,SBKZTXX,RLSB,GPS,SWPD,RXPD,ZTPW,CPAB,TSZD,TSRY\"}";
            if ("02".Equals(key2))
                return "{\"NeedInfo\":\"A1,A2,C1,C2,A4,A5,J1,J2\",\"InterfaceOrder\":\"GATXXX,RLSB,GPS,SWPD,RXPD,ZTPW\"}";
            if ("03".Equals(key2))
                return "{\"NeedInfo\":\"A1,C1,C2,A4,A5,C3,C4\",\"InterfaceOrder\":\"HGJBXX,HGTXXX,RLSB,GPS,SWPD,RXPD,ZTPW\"}";
            return "fault";
        }

    }
}
