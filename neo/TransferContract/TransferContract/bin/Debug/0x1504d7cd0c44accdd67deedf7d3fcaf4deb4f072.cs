using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Services.Neo;
using Neo.SmartContract.Framework.Services.System;
using System;
using System.Numerics;

namespace TransferContract
{
    public class TransferContract : SmartContract
    {
        //合约地址
        public static readonly byte[] Owner = "AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL".ToScriptHash();
        //易保
        public static readonly byte[] yibao = "AbyKBcoJPdW46DLGUUHJJ8vtVntrF6UirT".ToScriptHash();
        //运营商
        public static readonly byte[] yys = "AQtAQPyzCRLTaZhsthdZgTBfq4ytr4JEf9".ToScriptHash();
        //医院
        public static readonly byte[] yiyuan = "AMKKJL563Ydnrz9s7L3QLVWVJYmVwWRrfC".ToScriptHash();
        //public static readonly string from = "AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL";
        //public static readonly string to1 = "AbyKBcoJPdW46DLGUUHJJ8vtVntrF6UirT";
        //public static readonly string to2 = "AQtAQPyzCRLTaZhsthdZgTBfq4ytr4JEf9";
        //public static readonly string to3 = "AMKKJL563Ydnrz9s7L3QLVWVJYmVwWRrfC";
        [Appcall("342c8b1242c195929b109079da947b1e973fe2be")]//ScriptHash
        public static extern bool HLMCall(string operation, object[] args);
        public static object Main(string operation, object[] args)
        {
            if (operation == "allotTKY") return allotTKY(args);
            if (operation == "get") return Helper.AsString(getInfo((string)args[0]));
            return "fault";
        }
        public static bool allotTKY(object[] args)
        {
            bool isSucc = true;
            //构造参数
            object[] args2 = new object[3];
            args2[0] = Owner;
            args2[1] = yibao;
            args2[2] = 2 * 100000000;
            isSucc = HLMCall("transfer", args2);//给易保转2个
            string transferInfo = "";
            if ((BigInteger)args[1] > 0)//如果调用接口成功，给运营商转1个
            {
                args2[1] = yys;
                args2[2] = 1 * 100000000;
                isSucc = HLMCall("transfer", args2);
                transferInfo = "[{\"type\": \"yb\",\"Fee\": \"2 \",\"from\":\"AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL\",\"to\":\"AbyKBcoJPdW46DLGUUHJJ8vtVntrF6UirT\"},{\"type\": \"yys\",\"Fee\":\"1\",\"from\":\"AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL\",\"to\":\"AQtAQPyzCRLTaZhsthdZgTBfq4ytr4JEf9\"}]";
            }
            else
            {
                args2[1] = yiyuan;
                args2[2] = 1 * 100000000;
                isSucc = HLMCall("transfer", args2);
                transferInfo = "[{\"type\": \"yb\",\"Fee\": \"2 \",\"from\":\"AcesbhiJs1orCB4ty5c4Pi2SBRiMUspvhL\",\"to\":\"AbyKBcoJPdW46DLGUUHJJ8vtVntrF6UirT\"},{\"type\": \"yy\",\"Fee\":\"1\",\"from\":\"AMKKJL563Ydnrz9s7L3QLVWVJYmVwWRrfC\",\"to\":\"AQtAQPyzCRLTaZhsthdZgTBfq4ytr4JEf9\"}]";
            }
            Storage.Put(Storage.CurrentContext, (string)args[0], transferInfo);
            return isSucc;
        }

        public static byte[] getInfo(string key)
        {
            return Storage.Get(Storage.CurrentContext, key);
        }
    }
}
