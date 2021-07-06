#pragma warning disable IDE0060

using Neo.Cryptography.ECC;
using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Native;
using Neo.SmartContract.Framework.Services;
using System;
using System.ComponentModel;
using System.Numerics;
namespace Neo.SmartContract
{
    [ManifestExtra("Author", "Hong_Zilie")]
    [ManifestExtra("Email", "zilie.qin@imt-atlantique.net")]
    [ManifestExtra("Description", "Neo Point System")]
    [SupportedStandards("NEP-11")]
    [ContractPermission("*", "onNEP11Payment")]
    public sealed class NameService : Framework.SmartContract
    {
        public delegate void OnTransferDelegate(UInt160 from, UInt160 to, BigInteger amount, ByteString tokenId);
        public delegate void TestDemo(int breakpoint, string res, BigInteger b);

        // public delegate void RandomeGenreDelegate();

        [DisplayName("Transfer")]
        public static event OnTransferDelegate OnTransfer;

        [DisplayName("TestEvent")]
        public static event TestDemo test;

        // [DisplayName("TestRandom")]
        // public static event RandomeGenreDelegate OnRandom;
        private const byte Prefix_TotalSupply = 0x00;
        private const byte Prefix_Balance = 0x01;
        private const byte Prefix_AccountToken = 0x02;
        // private const byte Prefix_RegisterPrice = 0x10;

        private const byte Prefix_UserInfo = 0x22;
        // private const byte Prefix_Root = 0x20;
        private const byte Prefix_Name = 0x21;

        private const byte Prefix_UserPoint= 0x20;
        
        // private const byte Prefix_Record = 0x22;
        // private static Transaction Tx => (Transaction) Runtime.ScriptContainer;
        // private const int NameMaxLength = 255;
        private const ulong OneMonth =  24 * 3600 * 1000;

        [Safe]
        public static string Symbol() => "NNS";

        [Safe]
        public static byte Decimals() => 0;

        [Safe]
        public static BigInteger TotalSupply() => (BigInteger)Storage.Get(Storage.CurrentContext, new byte[] { Prefix_TotalSupply });



        [Safe]
        public static UInt160 OwnerOf(ByteString tokenId)
        {
            StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
            NameState token = (NameState)StdLib.Deserialize(nameMap[GetKey(tokenId)]);
            token.EnsureNotExpired();
            return token.Owner;
        }

        [Safe]
        public static Map<string, object> Properties(ByteString tokenId)
        {
            StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
            NameState token = (NameState)StdLib.Deserialize(nameMap[GetKey(tokenId)]);
            token.EnsureNotExpired();
            Map<string, object> map = new();
            map["name"] = token.Name;
            map["expiration"] = token.Expiration;
            map["owner"] = token.Owner;
            map["isOnsale"] = token.IsOnSale;
            map["Genre"] = token.Genre;
            map["Hp"] = token.Hp;
            map["Attack"] = token.Attack;
            map["Defense"] = token.Defense;
            return map;
        }

        [Safe]
        public static BigInteger BalanceOf(UInt160 owner)
        {
            if (owner is null)
                throw new Exception("The argument \"owner\" is invalid.");

            if (!owner.IsValid)
                throw new Exception("The argument \"owner\" is invalid.");
            StorageMap balanceMap = new(Storage.CurrentContext, Prefix_Balance);
            return (BigInteger)balanceMap[owner];
        }

        [Safe]
        public static Iterator Tokens()
        {
            StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
            return nameMap.Find(FindOptions.ValuesOnly | FindOptions.DeserializeValues | FindOptions.PickField1);
        }

        public static Iterator TokensOf(UInt160 owner)
        {
            if (owner is null || !owner.IsValid)
                throw new Exception("The argument \"owner\" is invalid.");
            StorageMap accountMap = new(Storage.CurrentContext, Prefix_AccountToken);
            return accountMap.Find(owner, FindOptions.ValuesOnly);
        }
        public static Object[] ShowMyItems(UInt160 owner)
        {
            var iterator = TokensOf(owner);
            Object[] MyItemsArray = new object[10];
            while (iterator.Next())
                {
                    var tokenId = iterator.Value;
                    Append(MyItemsArray,tokenId);
                }
            return MyItemsArray;
        }
        public static Object[] showItems()
        {
            var iterator = Tokens();
            Object[] ItemsArray = new object[10];
            while (iterator.Next()){
                
                var tokenId = iterator.Value;
                Append(ItemsArray,tokenId);
            }
            return ItemsArray;
        }

        public static bool TransferItem(UInt160 to, ByteString tokenId)
        {
            if (to is null || !to.IsValid)
                throw new Exception("The argument \"to\" is invalid.");
            StorageContext context = Storage.CurrentContext;
            StorageMap balanceMap = new(context, Prefix_Balance);
            StorageMap accountMap = new(context, Prefix_AccountToken);
            StorageMap nameMap = new(context, Prefix_Name);
            ByteString tokenKey = GetKey(tokenId);
            NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
            token.EnsureNotExpired();
            UInt160 from = token.Owner;
            // if (!Runtime.CheckWitness(from)) return false;
            if (from != to)
            {
                //Update token info
                token.Owner = to;
                nameMap[tokenKey] = StdLib.Serialize(token);

                //Update from account
                BigInteger balance = (BigInteger)balanceMap[from];
                balance--;
                if (balance.IsZero)
                    balanceMap.Delete(from);
                else
                    balanceMap.Put(from, balance);
                accountMap.Delete(from + tokenKey);

                //Update to account
                balance = (BigInteger)balanceMap[to];
                balance++;
                balanceMap.Put(to, balance);
                accountMap[to + tokenKey] = tokenId;
            }
            // PostTransfer(from, to, tokenId);
            return true;
        }


        public static void OnSale(UInt160 owner, ByteString tokenId) {
            if (owner is null || !owner.IsValid)
                throw new Exception("The argument \"owner\" is invalid.");
            if (!Runtime.CheckWitness(owner)) throw new InvalidOperationException("No authorization.");
            StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
            ByteString tokenKey = GetKey(tokenId);
            NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
            token.EnsureNotExpired();
            if(token.Owner != owner) {
                throw new Exception("this token does not belong to this user");
            }
            token.IsOnSale = true;
            nameMap[tokenKey] = StdLib.Serialize(token);
        
        }

        public static void CancelOnSale(UInt160 owner, ByteString tokenId) {
            if (owner is null || !owner.IsValid)
                throw new Exception("The argument \"owner\" is invalid.");
            if (!Runtime.CheckWitness(owner)) throw new InvalidOperationException("No authorization.");
            StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
            ByteString tokenKey = GetKey(tokenId);
            NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
            token.EnsureNotExpired();
            if(token.Owner != owner) {
                throw new Exception("this token does not belong to this user");
            }
            token.IsOnSale = false;
            nameMap[tokenKey] = StdLib.Serialize(token);
        
        }
        public static void Update(ByteString nef, string manifest)
        {
            CheckCommittee();
            ContractManagement.Update(nef, manifest);
        }

       

        // public static void SetPrice(long price)
        // {
        //     CheckCommittee();
        //     if (price <= 0 || price > 10000_00000000) throw new Exception("The price is out of range.");
        //     Storage.Put(Storage.CurrentContext, new byte[] { Prefix_RegisterPrice }, price);
        // }

        // [Safe]
        // public static long GetPrice()
        // {
        //     return (long)(BigInteger)Storage.Get(Storage.CurrentContext, new byte[] { Prefix_RegisterPrice });
        // }

       

        public static bool Register(string name, UInt160 owner)
        {
            StorageContext context = Storage.CurrentContext;
            StorageMap balanceMap = new(context, Prefix_Balance);
            StorageMap accountMap = new(context, Prefix_AccountToken);
            StorageMap nameMap = new(context, Prefix_Name);
            if (!Runtime.CheckWitness(owner)) throw new InvalidOperationException("No authorization.");
            // test(Tx.Sender, owner, GetPrice());
            // Runtime.BurnGas(GetPrice());
            
            ByteString tokenKey = GetKey(name);
            NameState token;
            UInt160 oldOwner = null;
            if (nameMap[tokenKey] is not null)
            {
                
                token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
                if (Runtime.Time < token.Expiration) return false;
                oldOwner = token.Owner;
                BigInteger balance = (BigInteger)balanceMap[oldOwner];
                balance--;
                if (balance.IsZero)
                    balanceMap.Delete(oldOwner);
                else
                    balanceMap.Put(oldOwner, balance);
                accountMap.Delete(oldOwner + tokenKey);
            }
            else
            {
                
                byte[] key = new byte[] { Prefix_TotalSupply };
                BigInteger totalSupply = (BigInteger)Storage.Get(context, key);
                Storage.Put(context, key, totalSupply + 1);
            }
            token = new()
            {
                Owner = owner,
                Name = name,
                IsOnSale = false,
                Expiration = Runtime.Time + OneMonth,
                Genre = RandomGenre(name),
                Hp =  RandomHp(name),
                Attack = RandomAttack(name),
                Defense = RandomDefense(name)
        
            };
            
            nameMap[tokenKey] = StdLib.Serialize(token);
            BigInteger ownerBalance = (BigInteger)balanceMap[owner];
            ownerBalance++;
            balanceMap.Put(owner, ownerBalance);
            accountMap[owner + tokenKey] = name;
            
            // PostTransfer(oldOwner, owner, name);
            return true;
        }

        [DisplayName("_deploy")]
        public static void OnDeployment(bool update)
        {
            if (update) return;
            StorageContext context = Storage.CurrentContext;
            Storage.Put(context, new byte[] { Prefix_TotalSupply }, 0);
            // Storage.Put(context, new byte[] { Prefix_RegisterPrice }, 10_00000000);
        }

        public static uint Random(String name) {
                Transaction tx = (Transaction) Runtime.ScriptContainer;
                test(0, "hello", 0);
                ByteString res = CryptoLib.Sha256(name + tx.Nonce.ToString());
                test(1, res, 1);
                BigInteger temp = (BigInteger) res;
                if (temp < 0 ) {
                    temp = - temp;
                }
                test(2, res, temp);
                return (uint)(temp % 100);
                
                // return  (uint)(Runtime.Time + tx.Nonce);
        }

        public static string RandomGenre(String name){
            String[] genres = {"hat","cloth","pants","shoses","pet"};
            long index  = (long)(Random(name) % (genres.Length));
            return genres[index];
        }

        public static uint RandomHp(String name){
            uint value  = (uint)Random(name) % 60;
            return value;
        }
        public static uint RandomAttack(String name){
            uint value  = (uint)Random(name) % 20;
            return value;
        }
        public static uint RandomDefense(String name){
            uint value  = (uint)Random(name) % 10;
            return value;
        }

        private static void CheckCommittee()
        {
            ECPoint[] committees = NEO.GetCommittee();
            UInt160 committeeMultiSigAddr = Contract.CreateMultisigAccount(committees.Length - (committees.Length - 1) / 2, committees);
            if (!Runtime.CheckWitness(committeeMultiSigAddr))
                throw new InvalidOperationException("No authorization.");
        }

        private static ByteString GetKey(string tokenId)
        {
            return CryptoLib.ripemd160(tokenId);
        }

        

        private static void PostTransfer(UInt160 from, UInt160 to, ByteString tokenId)
        {
            OnTransfer(from, to, 1, tokenId);
            if (to is not null && ContractManagement.GetContract(to) is not null)
                Contract.Call(to, "onNEP11Payment", CallFlags.All, from, 1, tokenId);
        }

        public static void Trigger(long time) {
            
            StorageMap UserMap = new StorageMap(Storage.CurrentContext,Prefix_UserInfo);
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
            if (UserMap[tx.Sender] is not null) {
                Users user = (Users)StdLib.Deserialize(UserMap[tx.Sender]);
                if(user.State == true) {
                    throw new Exception("A task has not finished or been canceled");
                }

                if(time <= 0 || time > 1000){
                    throw new Exception("out of limited time.");
                }
            
                //构造用户实例，传入参数{计时开始时间，结束时间初始值0，历史计时ArrayList,积分初始值0，当前没有计时任务false}
                user.StartTimeStamp = Runtime.Time;
                user.EndTimeStamp = 0;
                user.Interval = time;
                user.State =  true;
                UserMap[tx.Sender] = StdLib.Serialize(user);
    
            } else {
                Users user = new(){
                    StartTimeStamp = Runtime.Time,
                    EndTimeStamp = 0,
                    Interval = time,
                    State = true

                };
                UserMap[tx.Sender] = StdLib.Serialize(user);
            }
            

        }
        [OpCode(OpCode.APPEND)]
        private static extern void Append<T>(T[] array, T newItem);
        public static bool End(){
           
            StorageMap UserMap = new StorageMap(Storage.CurrentContext,Prefix_UserInfo);
            StorageMap PointMap = new StorageMap(Storage.CurrentContext,Prefix_UserPoint);
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
            Users user = (Users)StdLib.Deserialize(UserMap[tx.Sender]);
            user.EndTimeStamp = Runtime.Time;
            //如果当前技术任务为true,结束操作失败
            if(user.State == false){
                throw new Exception("Time task has not yet start!");
            }
            //如果结束时间-开始时间大于用户设置管理时间，认为用户完成时间管理，把此次计时任务存在用户任务历史总，并且获得积分
            if((user.EndTimeStamp - user.StartTimeStamp) >= (ulong)user.Interval){
                Append(user.History,user.StartTimeStamp);
                Append(user.History,user.EndTimeStamp);
                Append(user.History,user.Interval);
                user.Point += (ulong)user.Interval;
                user.State = false;
                UserMap[tx.Sender] = StdLib.Serialize(user);
                PointMap.Put(tx.Sender,(BigInteger)user.Point);
                return true;
                
            }else {
                return false;
            }
        }

        public static BigInteger PointsOf(UInt160 owner) {
            if (owner is null)
                throw new Exception("The argument \"owner\" is invalid.");

            if (!owner.IsValid)
                throw new Exception("The argument \"owner\" is invalid.");
            StorageMap PointMap = new StorageMap(Storage.CurrentContext,Prefix_UserPoint);
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
            if(PointMap.Get(owner) != null){
                return (BigInteger)PointMap.Get(owner);
            }
            else {
                return (BigInteger) 0;
            }
            
        }

        public static bool Cancel(){
            StorageMap UserMap = new StorageMap(Storage.CurrentContext,Prefix_UserInfo);
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
            Users user = (Users)StdLib.Deserialize(UserMap[tx.Sender]);
            if (user.State == false) {
                throw new Exception("The time management has already finished ");
            }
            user.State = false;
            UserMap[tx.Sender] = StdLib.Serialize(user);
            return user.State;
        }

        public static void WithDraw(int exchange,string name) {
            
            if (exchange < 0 || exchange > 10000) {
                throw new Exception(" out of exchange range.");
            }
            StorageMap PointMap = new StorageMap(Storage.CurrentContext,Prefix_UserPoint);
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
            var point = ((BigInteger)PointMap[tx.Sender]);
            if(point < exchange) {
                throw new Exception("no sufficient point.");
            }
            point = point - exchange;
            PointMap.Put(tx.Sender,(BigInteger)point);
            Register(name,tx.Sender);
        }  
        //Easier to get more points 
        public static void setPoint(int point) {
            Transaction tx = (Transaction)Runtime.ScriptContainer; // 提取tx的参数
             StorageMap PointMap = new StorageMap(Storage.CurrentContext,Prefix_UserPoint);
             PointMap.Put(tx.Sender,point);
        }
    }

    // private static byte[] GetRecordKey(ByteString tokenKey, string name, RecordType type)
        // {
        //     byte[] key = Helper.Concat((byte[])tokenKey, GetKey(name));
        //     return Helper.Concat(key, ((byte)type).ToByteArray());
        // }

     // [Safe]
        // public static bool IsAvailable(string name)
        // {
        //     StorageContext context = Storage.CurrentContext;
        //     StorageMap rootMap = new(context, Prefix_Root);
        //     StorageMap nameMap = new(context, Prefix_Name);
        //     // string[] fragments = SplitAndCheck(name, false);
        //     // if (fragments is null) throw new FormatException("The format of the name is incorrect.");
        //     // if (rootMap[fragments[^1]] is null) throw new Exception("The root does not exist.");
        //     ByteString buffer = nameMap[GetKey(name)];
        //     if (buffer is null) return true;
        //     NameState token = (NameState)StdLib.Deserialize(buffer);
        //     return Runtime.Time >= token.Expiration;
        // }
}

         // public static void AddRoot(string root)
        // {
        //     CheckCommittee();
        //     if (!CheckFragment(root, true))
        //         throw new FormatException("The format of the root is incorrect.");
        //     StorageMap rootMap = new(Storage.CurrentContext, Prefix_Root);
        //     if (rootMap[root] is not null)
        //         throw new InvalidOperationException("The root already exists.");
        //     rootMap.Put(root, 0);
        // }

        // [Safe]
        // public static Iterator Roots()
        // {
        //     StorageMap rootMap = new(Storage.CurrentContext, Prefix_Root);
        //     return rootMap.Find(FindOptions.KeysOnly | FindOptions.RemovePrefix);
        // }

        // public static ulong Renew(string name)
        // {
        //     if (name.Length > NameMaxLength) throw new FormatException("The format of the name is incorrect.");
        //     Runtime.BurnGas(GetPrice());
        //     StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
        //     ByteString tokenKey = GetKey(name);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     token.Expiration += OneYear;
        //     nameMap[tokenKey] = StdLib.Serialize(token);
        //     return token.Expiration;
        // }

        // public static void SetAdmin(string name, UInt160 admin)
        // {
        //     if (name.Length > NameMaxLength) throw new FormatException("The format of the name is incorrect.");
        //     if (admin is not null && !Runtime.CheckWitness(admin)) throw new InvalidOperationException("No authorization.");
        //     StorageMap nameMap = new(Storage.CurrentContext, Prefix_Name);
        //     ByteString tokenKey = GetKey(name);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     if (!Runtime.CheckWitness(token.Owner)) throw new InvalidOperationException("No authorization.");
        //     token.Admin = admin;
        //     nameMap[tokenKey] = StdLib.Serialize(token);
        // }

        // public static void SetRecord(string name, RecordType type, string data)
        // {
        //     StorageContext context = Storage.CurrentContext;
        //     StorageMap nameMap = new(context, Prefix_Name);
        //     StorageMap recordMap = new(context, Prefix_Record);
        //     string[] fragments = SplitAndCheck(name, true);
        //     if (fragments is null) throw new FormatException("The format of the name is incorrect.");
        //     switch (type)
        //     {
        //         case RecordType.A:
        //             if (!CheckIPv4(data)) throw new FormatException();
        //             break;
        //         case RecordType.CNAME:
        //             if (SplitAndCheck(data, true) is null) throw new FormatException();
        //             break;
        //         case RecordType.TXT:
        //             if (data.Length > 255) throw new FormatException();
        //             break;
        //         case RecordType.AAAA:
        //             if (!CheckIPv6(data)) throw new FormatException();
        //             break;
        //         default:
        //             throw new InvalidOperationException();
        //     }
        //     string tokenId = name[^(fragments[^2].Length + fragments[^1].Length + 1)..];
        //     ByteString tokenKey = GetKey(tokenId);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     token.CheckAdmin();
        //     byte[] recordKey = GetRecordKey(tokenKey, name, type);
        //     recordMap[recordKey] = data;
        // }

        // [Safe]
        // public static string GetRecord(string name, RecordType type)
        // {
        //     StorageContext context = Storage.CurrentContext;
        //     StorageMap nameMap = new(context, Prefix_Name);
        //     StorageMap recordMap = new(context, Prefix_Record);
        //     string[] fragments = SplitAndCheck(name, true);
        //     if (fragments is null) throw new FormatException("The format of the name is incorrect.");
        //     string tokenId = name[^(fragments[^2].Length + fragments[^1].Length + 1)..];
        //     ByteString tokenKey = GetKey(tokenId);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     byte[] recordKey = GetRecordKey(tokenKey, name, type);
        //     return recordMap[recordKey];
        // }

        // public static void DeleteRecord(string name, RecordType type)
        // {
        //     StorageContext context = Storage.CurrentContext;
        //     StorageMap nameMap = new(context, Prefix_Name);
        //     StorageMap recordMap = new(context, Prefix_Record);
        //     string[] fragments = SplitAndCheck(name, true);
        //     if (fragments is null) throw new FormatException("The format of the name is incorrect.");
        //     string tokenId = name[^(fragments[^2].Length + fragments[^1].Length + 1)..];
        //     ByteString tokenKey = GetKey(tokenId);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     token.CheckAdmin();
        //     byte[] recordKey = GetRecordKey(tokenKey, name, type);
        //     recordMap.Delete(recordKey);
        // }

        // [Safe]
        // public static string Resolve(string name, RecordType type)
        // {
        //     return Resolve(name, type, 2);
        // }

        // private static string Resolve(string name, RecordType type, int redirect)
        // {
        //     if (redirect < 0) throw new InvalidOperationException();
        //     string cname = null;
        //     foreach (var (key, value) in GetRecords(name))
        //     {
        //         RecordType rt = (RecordType)key[^1];
        //         if (rt == type) return value;
        //         if (rt == RecordType.CNAME) cname = value;
        //     }
        //     if (cname is null) return null;
        //     return Resolve(cname, type, redirect - 1);
        // }

        // private static Iterator<(ByteString, string)> GetRecords(string name)
        // {
        //     StorageContext context = Storage.CurrentContext;
        //     StorageMap nameMap = new(context, Prefix_Name);
        //     StorageMap recordMap = new(context, Prefix_Record);
        //     string[] fragments = SplitAndCheck(name, true);
        //     if (fragments is null) throw new FormatException("The format of the name is incorrect.");
        //     string tokenId = name[^(fragments[^2].Length + fragments[^1].Length + 1)..];
        //     ByteString tokenKey = GetKey(tokenId);
        //     NameState token = (NameState)StdLib.Deserialize(nameMap[tokenKey]);
        //     token.EnsureNotExpired();
        //     byte[] recordKey = Helper.Concat((byte[])tokenKey, GetKey(name));
        //     return (Iterator<(ByteString, string)>)recordMap.Find(recordKey);
        // }
 

        // private static bool CheckFragment(string root, bool isRoot)
        // {
        //     int maxLength = isRoot ? 16 : 62;
        //     if (root.Length == 0 || root.Length > maxLength) return false;
        //     char c = root[0];
        //     if (isRoot)
        //     {
        //         if (!(c >= 'a' && c <= 'z')) return false;
        //     }
        //     else
        //     {
        //         if (!(c >= 'a' && c <= 'z' || c >= '0' && c <= '9')) return false;
        //     }
        //     for (int i = 1; i < root.Length; i++)
        //     {
        //         c = root[i];
        //         if (!(c >= 'a' && c <= 'z' || c >= '0' && c <= '9')) return false;
        //     }
        //     return true;
        // }

        // private static string[] SplitAndCheck(string name, bool allowMultipleFragments)
        // {
        //     int length = name.Length;
        //     if (length < 3 || length > NameMaxLength) return null;
        //     string[] fragments = StdLib.StringSplit(name, ".");
        //     length = fragments.Length;
        //     if (length < 2) return null;
        //     if (length > 2 && !allowMultipleFragments) return null;
        //     for (int i = 0; i < length; i++)
        //         if (!CheckFragment(fragments[i], i == length - 1))
        //             return null;
        //     return fragments;
        // }

        // private static bool CheckIPv4(string ipv4)
        // {
        //     int length = ipv4.Length;
        //     if (length < 7 || length > 15) return false;
        //     string[] fragments = StdLib.StringSplit(ipv4, ".");
        //     length = fragments.Length;
        //     if (length != 4) return false;
        //     byte[] numbers = new byte[4];
        //     for (int i = 0; i < length; i++)
        //     {
        //         string fragment = fragments[i];
        //         if (fragment.Length == 0) return false;
        //         byte number = byte.Parse(fragment);
        //         if (number > 0 && fragment[0] == '0') return false;
        //         if (number == 0 && fragment.Length > 1) return false;
        //         numbers[i] = number;
        //     }
        //     switch (numbers[0])
        //     {
        //         case 0:
        //         case 10:
        //         case 127:
        //         case >= 224:
        //             return false;
        //         case 169:
        //             if (numbers[1] == 254) return false;
        //             break;
        //         case 172:
        //             if (numbers[1] >= 16 && numbers[1] <= 31) return false;
        //             break;
        //         case 192:
        //             if (numbers[1] == 168) return false;
        //             break;
        //     }
        //     return numbers[3] switch
        //     {
        //         0 or 255 => false,
        //         _ => true,
        //     };
        // }

        // private static bool CheckIPv6(string ipv6)
        // {
        //     int length = ipv6.Length;
        //     if (length < 2 || length > 39) return false;
        //     string[] fragments = StdLib.StringSplit(ipv6, ":");
        //     length = fragments.Length;
        //     if (length < 3 || length > 8) return false;
        //     ushort[] numbers = new ushort[8];
        //     bool hasEmpty = false;
        //     for (int i = 0; i < length; i++)
        //     {
        //         string fragment = fragments[i];
        //         if (fragment.Length == 0)
        //         {
        //             if (i == 0)
        //             {
        //                 numbers[0] = 0;
        //             }
        //             else if (i == length - 1)
        //             {
        //                 numbers[7] = 0;
        //             }
        //             else if (hasEmpty)
        //             {
        //                 return false;
        //             }
        //             else
        //             {
        //                 hasEmpty = true;
        //                 int endIndex = 9 - length + i;
        //                 for (int j = i; j < endIndex; j++)
        //                     numbers[j] = 0;
        //             }
        //         }
        //         else
        //         {
        //             if (fragment.Length > 4) return false;
        //             int index = hasEmpty ? i + 8 - length : i;
        //             numbers[index] = (ushort)StdLib.Atoi(fragment, 16);
        //         }
        //     }
        //     ushort number = numbers[0];
        //     if (number < 0x2000 || number == 0x2002 || number == 0x3ffe || number > 0x3fff)
        //         return false;
        //     if (number == 0x2001)
        //     {
        //         number = numbers[1];
        //         if (number < 0x200 || number == 0xdb8) return false;
        //     }
        //     return true;
    
    
// }
