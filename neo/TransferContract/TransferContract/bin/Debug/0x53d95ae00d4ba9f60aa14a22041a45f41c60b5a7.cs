using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Services.Neo;
using System;
using System.Numerics;

namespace TransferContract
{
    public class TransferContract : SmartContract
    {
        [Appcall("307833306162326461646239346265396331326637653637366331326564623265353864386337633839")]//ScriptHash
        public static extern bool transfer(byte[] from, byte[] to, BigInteger amount);

        public static readonly byte[] Owner = "AMZiiAnBg5uFLXGVMSt4hPEA2ubRCiNZg5".ToScriptHash();

        public static object Main(string operation, object[] args)
        {

            if (Runtime.Trigger == TriggerType.Verification)
            {
                return Runtime.CheckWitness(Owner);
            }
            else if (Runtime.Trigger == TriggerType.Application)
            {
                if (operation == "transfer") return transfer((byte[])args[0], (byte[])args[1], (BigInteger)args[2]);
            }
            return "fault";
        }
    }
}
