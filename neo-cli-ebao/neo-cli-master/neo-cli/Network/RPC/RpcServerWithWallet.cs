using Neo.Core;
using Neo.Implementations.Wallets.NEP6;
using Neo.IO;
using Neo.IO.Json;
using Neo.SmartContract;
using Neo.Wallets;
using System.Collections.Generic;
using System.IO;
using System.Linq;

//Try open wallet with RPC
using System.Security.Cryptography;

namespace Neo.Network.RPC
{
    internal class RpcServerWithWallet : RpcServer
    {
        public RpcServerWithWallet(LocalNode localNode)
            : base(localNode)
        {
        }
        public static string Base64Decode(string result)
        {
            return Base64Decode(System.Text.Encoding.UTF8, result);
        }
        public static string Base64Decode(System.Text.Encoding encodeType, string result)
        {
            string decode = string.Empty;
            byte[] bytes = System.Convert.FromBase64String(result);
            try
            {
                decode = encodeType.GetString(bytes);
            }
            catch
            {
                decode = result;
            }
            return decode;
        }
        public static string Base64Encode(string source)
        {
            return Base64Encode(System.Text.Encoding.UTF8, source);
        }


        public static string Base64Encode(System.Text.Encoding encodeType, string source)
        {
            string encode = string.Empty;
            byte[] bytes = encodeType.GetBytes(source);
            try
            {
                encode = System.Convert.ToBase64String(bytes);
            }
            catch
            {
                encode = source;
            }
            return encode;
        }


        protected override JObject Process(string method, JArray _params)
        {
            switch (method)
            {
                //{"name":null,"version":"1.0","scrypt":{"n":16384,"r":8,"p":8},"accounts":[{"address":"AHGVqc2L3vbxG8N6t76oxg8ihdyxcqePB1","label":null,"isDefault":false,"lock":false,"key":"6PYMhaNsXjhGALn3XfEqwFLr3VsbCmgfr1WHMjaNgS5C9677L4KNoih46G","contract":{"script":"210331cef9faf71d85c4dd19911dd7d53815dc69056322f1db62a66df264e5f8ba8cac","parameters":[{"name":"signature","type":"Signature"}],"deployed":false},"extra":null},{"address":"ARBFVamaqvmyVjyN6P8V3tGeoZZJBZ7drP","label":null,"isDefault":false,"lock":false,"key":"6PYSMmydUaZCiYa6B8AgGJYDsXXfTmr1PdG27uWPkjtsdv4n3K2tbQ8WM1","contract":{"script":"2102ca41146dba006ef61e328e0735bdb46e76ee12f8feb94eec0f5cfb1bcf57ee17ac","parameters":[{"name":"signature","type":"Signature"}],"deployed":false},"extra":null},{"address":"AJ3uJEYBudv5ZFo2Eg6MFP1ZzimefzD8vZ","label":null,"isDefault":false,"lock":false,"key":"6PYWzN8pz45xxQu4wkQtn8GwvbfUhDS9iRBDgdLt5Vwd1RgkU6SAtuApuR","contract":{"script":"2103f4ca1a9ab97e8cdcd6349b72ff0914f5b70774a2c6927d4c0db1b2d733c696bcac","parameters":[{"name":"signature","type":"Signature"}],"deployed":false},"extra":null}],"extra":null}
                //将整个钱包的json作为一个串放进param
                // "{'jsonrpc': '2.0', 'method': 'openwallet', 'params': ["walllet-base64-json","password"],  'id': 1}"


                case "openwallet":
                    {
                        string oStr = Base64Decode(_params[0].AsString());
                        //System.Console.WriteLine(_params[0].AsString());
                        //System.Console.WriteLine(oStr);
                        //JObject walletJson = _params[0];
                        string password = Base64Decode(_params[1].AsString()); 

                        string path = Directory.GetCurrentDirectory() + "\\" + "tmp.json";

                        FileStream F = new FileStream("tmp.json", FileMode.OpenOrCreate, FileAccess.ReadWrite);
                        F.SetLength(0);
                        StreamWriter sw = new StreamWriter(F);
                        //sw.Write(walletJson.AsString());
                        sw.Write(oStr);
                        sw.Flush();
                        sw.Close();

                        JObject json = new JObject();

                        NEP6Wallet nep6wallet = new NEP6Wallet(path);
                        try
                        {
                            nep6wallet.Unlock(password);
                        }
                        catch (CryptographicException)
                        {
                            System.Console.WriteLine($"failed to open wallet \"{path}\"");
                            File.Delete(path);
                            throw new RpcException(-400, "Access denied");                            
                        }

                        Program.Wallet = nep6wallet;
                        
                        File.Delete(path); //打开后删除钱包文件
                        return "0";
                    }
                case "closewallet":
                    {
                       //对当前打开的钱包做校验
                       // if(Program.Wallet != null)
                       // {
                       //     Base64Encode
                       // }



                        Program.Wallet = null;
                        return "0";
                    }

                case "getapplicationlog":
                    {
                        UInt256 hash = UInt256.Parse(_params[0].AsString());
                        string path = Path.Combine(Settings.Default.Paths.ApplicationLogs, $"{hash}.json");
                        return File.Exists(path)
                            ? JObject.Parse(File.ReadAllText(path))
                            : throw new RpcException(-100, "Unknown transaction");
                    }
                case "getbalance":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied.");
                    else
                    {
                        JObject json = new JObject();
                        switch (UIntBase.Parse(_params[0].AsString()))
                        {
                            case UInt160 asset_id_160: //NEP-5 balance
                                json["balance"] = Program.Wallet.GetAvailable(asset_id_160).ToString();
                                break;
                            case UInt256 asset_id_256: //Global Assets balance
                                IEnumerable<Coin> coins = Program.Wallet.GetCoins().Where(p => !p.State.HasFlag(CoinState.Spent) && p.Output.AssetId.Equals(asset_id_256));
                                json["balance"] = coins.Sum(p => p.Output.Value).ToString();
                                json["confirmed"] = coins.Where(p => p.State.HasFlag(CoinState.Confirmed)).Sum(p => p.Output.Value).ToString();
                                break;
                        }
                        return json;
                    }
                case "listaddress":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied.");
                    else
                        return Program.Wallet.GetAccounts().Select(p =>
                        {
                            JObject account = new JObject();
                            account["address"] = p.Address;
                            account["haskey"] = p.HasKey;
                            account["label"] = p.Label;
                            account["watchonly"] = p.WatchOnly;
                            return account;
                        }).ToArray();
                case "sendfrom":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied");
                    else
                    {
                        UIntBase assetId = UIntBase.Parse(_params[0].AsString());
                        AssetDescriptor descriptor = new AssetDescriptor(assetId);
                        UInt160 from = Wallet.ToScriptHash(_params[1].AsString());
                        UInt160 to = Wallet.ToScriptHash(_params[2].AsString());
                        BigDecimal value = BigDecimal.Parse(_params[3].AsString(), descriptor.Decimals);
                        if (value.Sign <= 0)
                            throw new RpcException(-32602, "Invalid params");
                        Fixed8 fee = _params.Count >= 5 ? Fixed8.Parse(_params[4].AsString()) : Fixed8.Zero;
                        if (fee < Fixed8.Zero)
                            throw new RpcException(-32602, "Invalid params");
                        UInt160 change_address = _params.Count >= 6 ? Wallet.ToScriptHash(_params[5].AsString()) : null;
                        Transaction tx = Program.Wallet.MakeTransaction(null, new[]
                        {
                            new TransferOutput
                            {
                                AssetId = assetId,
                                Value = value,
                                ScriptHash = to
                            }
                        }, from: from, change_address: change_address, fee: fee);
                        if (tx == null)
                            throw new RpcException(-300, "Insufficient funds");
                        ContractParametersContext context = new ContractParametersContext(tx);
                        Program.Wallet.Sign(context);
                        if (context.Completed)
                        {
                            tx.Scripts = context.GetScripts();
                            Program.Wallet.ApplyTransaction(tx);
                            LocalNode.Relay(tx);
                            return tx.ToJson();
                        }
                        else
                        {
                            return context.ToJson();
                        }
                    }
                case "sendtoaddress":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied");
                    else
                    {
                        UIntBase assetId = UIntBase.Parse(_params[0].AsString());
                        AssetDescriptor descriptor = new AssetDescriptor(assetId);
                        UInt160 scriptHash = Wallet.ToScriptHash(_params[1].AsString());
                        BigDecimal value = BigDecimal.Parse(_params[2].AsString(), descriptor.Decimals);
                        if (value.Sign <= 0)
                            throw new RpcException(-32602, "Invalid params");
                        Fixed8 fee = _params.Count >= 4 ? Fixed8.Parse(_params[3].AsString()) : Fixed8.Zero;
                        if (fee < Fixed8.Zero)
                            throw new RpcException(-32602, "Invalid params");
                        UInt160 change_address = _params.Count >= 5 ? Wallet.ToScriptHash(_params[4].AsString()) : null;
                        Transaction tx = Program.Wallet.MakeTransaction(null, new[]
                        {
                            new TransferOutput
                            {
                                AssetId = assetId,
                                Value = value,
                                ScriptHash = scriptHash
                            }
                        }, change_address: change_address, fee: fee);
                        if (tx == null)
                            throw new RpcException(-300, "Insufficient funds");
                        ContractParametersContext context = new ContractParametersContext(tx);
                        Program.Wallet.Sign(context);
                        if (context.Completed)
                        {
                            tx.Scripts = context.GetScripts();
                            Program.Wallet.ApplyTransaction(tx);
                            LocalNode.Relay(tx);
                            return tx.ToJson();
                        }
                        else
                        {
                            return context.ToJson();
                        }
                    }
                case "sendmany":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied");
                    else
                    {
                        JArray to = (JArray)_params[0];
                        if (to.Count == 0)
                            throw new RpcException(-32602, "Invalid params");
                        TransferOutput[] outputs = new TransferOutput[to.Count];
                        for (int i = 0; i < to.Count; i++)
                        {
                            UIntBase asset_id = UIntBase.Parse(to[i]["asset"].AsString());
                            AssetDescriptor descriptor = new AssetDescriptor(asset_id);
                            outputs[i] = new TransferOutput
                            {
                                AssetId = asset_id,
                                Value = BigDecimal.Parse(to[i]["value"].AsString(), descriptor.Decimals),
                                ScriptHash = Wallet.ToScriptHash(to[i]["address"].AsString())
                            };
                            if (outputs[i].Value.Sign <= 0)
                                throw new RpcException(-32602, "Invalid params");
                        }
                        Fixed8 fee = _params.Count >= 2 ? Fixed8.Parse(_params[1].AsString()) : Fixed8.Zero;
                        if (fee < Fixed8.Zero)
                            throw new RpcException(-32602, "Invalid params");
                        UInt160 change_address = _params.Count >= 3 ? Wallet.ToScriptHash(_params[2].AsString()) : null;
                        Transaction tx = Program.Wallet.MakeTransaction(null, outputs, change_address: change_address, fee: fee);
                        if (tx == null)
                            throw new RpcException(-300, "Insufficient funds");
                        ContractParametersContext context = new ContractParametersContext(tx);
                        Program.Wallet.Sign(context);
                        if (context.Completed)
                        {
                            tx.Scripts = context.GetScripts();
                            Program.Wallet.ApplyTransaction(tx);
                            LocalNode.Relay(tx);
                            return tx.ToJson();
                        }
                        else
                        {
                            return context.ToJson();
                        }
                    }
                case "getnewaddress":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied");
                    else
                    {
                        WalletAccount account = Program.Wallet.CreateAccount();
                        if (Program.Wallet is NEP6Wallet wallet)
                            wallet.Save();
                        return account.Address;
                    }
                case "dumpprivkey":
                    if (Program.Wallet == null)
                        throw new RpcException(-400, "Access denied");
                    else
                    {
                        UInt160 scriptHash = Wallet.ToScriptHash(_params[0].AsString());
                        WalletAccount account = Program.Wallet.GetAccount(scriptHash);
                        return account.GetKey().Export();
                    }
                case "invoke":
                case "invokefunction":
                case "invokescript":
                    JObject result = base.Process(method, _params);
                    if (Program.Wallet != null)
                    {
                        InvocationTransaction tx = new InvocationTransaction
                        {
                            Version = 1,
                            Script = result["script"].AsString().HexToBytes(),
                            Gas = Fixed8.Parse(result["gas_consumed"].AsString())
                        };
                        tx.Gas -= Fixed8.FromDecimal(10);
                        if (tx.Gas < Fixed8.Zero) tx.Gas = Fixed8.Zero;
                        tx.Gas = tx.Gas.Ceiling();
                        tx = Program.Wallet.MakeTransaction(tx);
                        if (tx != null)
                        {
                            ContractParametersContext context = new ContractParametersContext(tx);
                            Program.Wallet.Sign(context);
                            if (context.Completed)
                                tx.Scripts = context.GetScripts();
                            else
                                tx = null;
                        }
                        result["tx"] = tx?.ToArray().ToHexString();
                    }
                    return result;
                default:
                    return base.Process(method, _params);
            }
        }
    }
}
