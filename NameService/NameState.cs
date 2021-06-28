using Neo.SmartContract.Framework.Services;
using System;

namespace Neo.SmartContract
{
    class NameState
    {
        public UInt160 Owner;
        public string Name;
        public ulong Expiration;
        public bool IsOnSale; 


        public void EnsureNotExpired()
        {
            if (Runtime.Time >= Expiration)
                throw new Exception("The item has expired.");
        }

    }
}
