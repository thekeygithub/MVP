using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace operateProxy
{
    public class SCDUtil : OperateType
    {
       // string api = "http://127.0.0.1:59908/api/privatenet";//
        string api = "http://192.168.70.25:59908/api/privatenet";//
       // string api = "http://47.96.168.8:81/api/testnet";//测试网

        public string Name => "拼交易";

        public string ID => "sc3";

        async public Task<string> Operation(string script, string prikeyStr, string method, List<string> args)
        {
            // string script = "0x342c8b1242c195929b109079da947b1e973fe2be";
            // string script = "0x96babb04e415a402b973350df71e10d24c725f78";
            //   string script = "0x55b300f8468d9e2bb9c3a4b8ff870c8c3b6ddf7c";//测试网


           // byte[] prikey = ThinNeo.Helper.GetPrivateKeyFromWIF(prikeyStr);
           // byte[] prikey = ThinNeo.Helper.GetPrivateKeyFromWIF("L3tDHnEAvwnnPE4sY4oXpTvNtNhsVhbkY4gmEmWmWWf1ebJhVPVW");//测试网
            byte[] prikey = ThinNeo.Helper.GetPrivateKeyFromWIF(prikeyStr);
            byte[] pubkey = ThinNeo.Helper.GetPublicKeyFromPrivateKey(prikey);
            string address = ThinNeo.Helper.GetAddressFromPublicKey(pubkey);
            //  string toaddr = "APwCdakS1NpJsiq6j9SfvkQFS9ubt347a2";
            string id_GAS = "0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";
         //   Console.WriteLine("prikey：" + prikeyStr);
            //获取地址的资产列表
            Dictionary<string, List<Utxo>> dir = await Helper.GetBalanceByAddress(api, address);
            string targeraddr = address;  //Transfer it to yourself. 
            ThinNeo.Transaction tran = Helper.makeTran(dir[id_GAS], targeraddr, new ThinNeo.Hash256(id_GAS), decimal.Zero);
            tran.type = ThinNeo.TransactionType.InvocationTransaction;
            ThinNeo.ScriptBuilder sb = new ThinNeo.ScriptBuilder();
            var scriptaddress = new ThinNeo.Hash160(script);
            //Parameter inversion 
            MyJson.JsonNode_Array JAParams = new MyJson.JsonNode_Array();
            var uuidN = "";
     
            //加入循环记录参数
            if ("saveValidateInfo".Equals(method)|| "savePayInfo".Equals(method))
            {
                uuidN = Guid.NewGuid().ToString("N");
                JAParams.Add(new MyJson.JsonNode_ValueString("(str)" + uuidN));
                JAParams.Add(new MyJson.JsonNode_ValueString("(str)" + args[0]));
            }
            else if ("saveResult".Equals(method))
            {
                uuidN = args[0];
                JAParams.Add(new MyJson.JsonNode_ValueString("(str)" + uuidN));
                JAParams.Add(new MyJson.JsonNode_ValueString("(str)" + args[1]));
            }
            else if ("transfer".Equals(method))
            {
                JAParams.Add(new MyJson.JsonNode_ValueString("(address)" + args[0]));
                JAParams.Add(new MyJson.JsonNode_ValueString("(address)" + args[1]));
                JAParams.Add(new MyJson.JsonNode_ValueString("(integer)" + args[2]));
            }
            else if ("allotTKY".Equals(method))
            {
                uuidN = Guid.NewGuid().ToString("N");
                JAParams.Add(new MyJson.JsonNode_ValueString("(str)" + uuidN));
                JAParams.Add(new MyJson.JsonNode_ValueString("(integer)" + args[0]));
                JAParams.Add(new MyJson.JsonNode_ValueString("(integer)" + args[1]));
            }
            else {
            }
            sb.EmitParamJson(JAParams);//Parameter list 
            if ("saveValidateInfo".Equals(method))
            {
                sb.EmitPushString("save");//Method
            }
            sb.EmitPushString(method);//Method
            sb.EmitAppCall(scriptaddress);  //Asset contract 
            //转账
            //加入循环记录参数
            //JAParams.Add(new MyJson.JsonNode_ValueString("(addr)AeP5KqSJxPEpWCGRfoDiCr3Xtg6jJbRbbH"));
            //JAParams.Add(new MyJson.JsonNode_ValueString("(addr)AMZiiAnBg5uFLXGVMSt4hPEA2ubRCiNZg5"));
            //JAParams.Add(new MyJson.JsonNode_ValueString("(integer)9"));
            //// JAParams.Add(new MyJson.JsonNode_ValueString("(integer)" + 1));
            //sb.EmitParamJson(JAParams);//Parameter list 
            ////方法名
            //sb.EmitPushString("transfer");//Method
            ThinNeo.InvokeTransData extdata = new ThinNeo.InvokeTransData();
            extdata.script = sb.ToArray();
            extdata.gas = 1;
            tran.extdata = extdata;
        
            byte[] msg = tran.GetMessage();
            byte[] signdata = ThinNeo.Helper.Sign(msg, prikey);
            tran.AddWitness(signdata, pubkey, address);
            string txid = tran.GetHash().ToString();
            byte[] data = tran.GetRawData();
            string scripthash = ThinNeo.Helper.Bytes2HexString(data);
            //String url2 = "http://47.96.168.8:20332";//测试网
            String url2 = "http://192.168.70.25:10332";
         //   Console.WriteLine("txid：" + txid);
           // Console.WriteLine("签名："+scripthash);
            // return scripthash;
            string response = await Helper.HttpGet(url2 + "?method=sendrawtransaction&id=1&params=[\"" + scripthash + "\"]");
            MyJson.JsonNode_Object resJO = (MyJson.JsonNode_Object)MyJson.Parse(response);
          //  Console.WriteLine(resJO["result"].ToString());
            string resultStr = "";
            if ("saveValidateInfo".Equals(method) || "savePayInfo".Equals(method))
            {
                resultStr="sendrawtransactionResult:" + resJO["result"].ToString() + ",chainKey:" + uuidN + ",txid:" + txid; ;
            }
            else if ("saveResult".Equals(method))
            {
                resultStr = "sendrawtransactionResult:" + resJO["result"].ToString() + ",chainKey:" + args[0] + "rs,txid:"+ txid;
            }
            else if ("transfer".Equals(method))
            {
                resultStr = "sendrawtransactionResult:" + resJO["result"].ToString();
            }
            else if ("allotTKY".Equals(method))
            {
                resultStr = "sendrawtransactionResult:" + resJO["result"].ToString() + ",chainKey:" + uuidN+",txid:"+ txid;
            }
            else
            {
            }
        //    Console.WriteLine(resultStr);
            return resultStr;
        }
        
    }

}
