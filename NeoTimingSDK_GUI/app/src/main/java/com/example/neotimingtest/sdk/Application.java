package com.example.neotimingtest.sdk;

import io.neow3j.contract.*;
import io.neow3j.contract.SmartContract;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.Neo;
import io.neow3j.protocol.core.response.NeoInvokeFunction;
import io.neow3j.protocol.core.response.NeoSendRawTransaction;
import io.neow3j.protocol.core.stackitem.ByteStringStackItem;
import io.neow3j.protocol.core.stackitem.MapStackItem;
import io.neow3j.protocol.core.stackitem.StackItem;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.Signer;
import io.neow3j.transaction.TransactionBuilder;
import io.neow3j.types.ContractParameter;
import io.neow3j.types.Hash160;
import io.neow3j.wallet.Account;
import io.neow3j.wallet.Wallet;


import java.math.BigInteger;
import java.util.*;


public class Application {


    private static Account account;
    //每次重新部署合约都需要一个新的合约地址
    //0x88d7efe029d9741690e3f91a2745062a636b21ec NameService合约地址
    //0xcf821812cba65f7219654ab9e1a29ed208ef8bb9 改完withdraw和 register之后的合约地址
    //0x32f78d5f43379c519fb9d6970c69c56b9713d809
    private static final String contractAddress = "0x9a57bcd856db097fb1332e6faa9eb1b09904e4ec";

    private static Neow3j neow3j;

    private static Wallet wallet;

    private static final Hash160 scriptHash = new Hash160(contractAddress);

    public static final String privateKeyWif = "KybmnDXHZaixQUnrwKBu4LZrEfRrYkGt5cKJpBSTkRjJ8Dqxw7PD";
    //每次重新部署合约后，需要给该地址转gas为了执行合约
    private static final String publickeyAddress = "NicfNxmzhd5f36z7N5Nap3HaT8Z5WB7WKq";

    public static void importWallet(String wif) {
        account = Account.fromWIF(wif);
        wallet = Wallet.withAccounts(account);

    }

    public static String createAccount() throws Throwable {
        Account account2 = Account.create();
        String wif = account2.getECKeyPair().exportAsWIF();
        System.out.println(account2.getECKeyPair().getPublicKey());
        System.out.println(account2.getECKeyPair().getAddress());
        System.out.println(account2.getECKeyPair().exportAsWIF());
        BigInteger amount = BigInteger.valueOf(1000);
//            NeoSendRawTransaction response = new GasToken(neow3j)
//                    .transferFromDefaultAccount(wallet, Hash160.fromAddress(account2.getAddress()),amount )
//                    .signers(Signer.calledByEntry(account.getScriptHash()))
//                    .wallet(wallet)
//                    .additionalNetworkFee(1L)
//                    .sign()
//                    .send();
        BigInteger res = new GasToken(neow3j).getBalanceOf(account2);
            System.out.println(res);
            return wif;
        }


    public static void trigger(int time) throws Throwable {
        time = time *60;
        String function = "trigger";
        ContractParameter timeParam = ContractParameter.integer(time);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, timeParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
    }

    public static void startConnection() {
        neow3j = Neow3j.build(new HttpService("http://192.168.1.47:20332"));


    }

    private static void checkConnection() throws Exception {
        if (neow3j == null) {
            throw new Exception("Connection has not been created!");
        }
    }

    public static void end() throws Throwable {
        String function = "end";
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();

    }

    public static BigInteger pointsOf(Hash160 address) throws Throwable {
        String function = "pointsOf";
        checkConnection();
        ContractParameter usrParam = ContractParameter.hash160(address);

        List<ContractParameter> params = Arrays.asList(usrParam);
        NeoInvokeFunction response = new SmartContract(scriptHash, neow3j)
                .callInvokeFunction(function, params, Signer.calledByEntry(account.getScriptHash()));
//        System.out.println(response.getInvocationResult().getStack().get(0));
//        System.out.println(response.getInvocationResult().getStack().get(0).getType());
        System.out.println(response.getInvocationResult().getStack().get(0).getValue());
        return (BigInteger) response.getInvocationResult().getStack().get(0).getValue();

    }

    public static BigInteger banlanceOf(Hash160 address) throws Throwable {
        String function = "balanceOf";
        checkConnection();
        ContractParameter usrParam = ContractParameter.hash160(address);
        List<ContractParameter> params = Arrays.asList(usrParam);
        NeoInvokeFunction response = new SmartContract(scriptHash, neow3j)
                .callInvokeFunction(function, params, Signer.calledByEntry(account.getScriptHash()));
        System.out.println(response.getInvocationResult().getStack().get(0).getValue());
        return (BigInteger) response.getInvocationResult().getStack().get(0).getValue();

    }

    public static void withdraw(int exchange,String name) throws Throwable {
        String function = "withDraw";
        ContractParameter exchangeParam = ContractParameter.integer(exchange);
        ContractParameter nameParam = ContractParameter.string(name);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, exchangeParam,nameParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash())).additionalNetworkFee(10)
                .wallet(wallet)
                .sign()
                .send();
    }


    public static void  cancel() throws Throwable {
        String function = "cancel";
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
    }

    public static void onSale(Hash160 address, String name) throws Throwable {
        String function = "onSale";
        ContractParameter usrParam = ContractParameter.hash160(address);
        ContractParameter nameParam = ContractParameter.string(name);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, usrParam,nameParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
    }

    public static void cancelOnSale(Hash160 address, String name) throws Throwable {
        String function = "cancelOnSale";
        ContractParameter usrParam = ContractParameter.hash160(address);
        ContractParameter nameParam = ContractParameter.string(name);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, usrParam,nameParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
    }

    public static void setPoint(int point) throws Throwable {
        String function = "setPoint";
        ContractParameter pointParam = ContractParameter.integer(point);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, pointParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
    }

    public static ArrayList<String> showMyItems(Hash160 address) throws Exception {
        String function = "showMyItems";
        checkConnection();
        ContractParameter usrParam = ContractParameter.hash160(address);
        List<ContractParameter> params = Arrays.asList(usrParam);
        NeoInvokeFunction response = new SmartContract(scriptHash, neow3j)
                .callInvokeFunction(function, params, Signer.calledByEntry(account.getScriptHash()));
        System.out.println(response.getInvocationResult().getStack().get(0).getValue().getClass());
        ArrayList res = (ArrayList) response.getInvocationResult().getStack().get(0).getValue();
        ArrayList<String> itemArrayList = new ArrayList<>();
        int count = res.size() - 1;
        while(res.get(count) instanceof ByteStringStackItem) {
            ByteStringStackItem rr = (ByteStringStackItem) res.get(count);
            itemArrayList.add(rr.getString());
            count --;
            System.out.println(rr.getString());

        }
        return itemArrayList;
    }

    public static Token tokenProperties(String name) throws Exception {
        String function = "properties";
        checkConnection();
        ContractParameter tokenParam = ContractParameter.string(name);
        List<ContractParameter> params = Arrays.asList(tokenParam);
        NeoInvokeFunction response = new SmartContract(scriptHash, neow3j)
                .callInvokeFunction(function, params, Signer.calledByEntry(account.getScriptHash()));

        MapStackItem mapStack = (MapStackItem) response.getInvocationResult().getStack().get(0);
        Token token = new Token();
        Set<StackItem> keySet = mapStack.getMap().keySet();
        for (StackItem key : keySet){
//           System.out.println(key.getString() +" --- " + mapStack.getMap().get(key).getString());
//            System.out.println(mapStack.getMap().get(key).getClass());
            if(key.getString().equals("name")) {
                token.setName(mapStack.getMap().get(key).getString());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getString());
            }
            if(key.getString().equals("expiration") ) {
                token.setExpiration(mapStack.getMap().get(key).getInteger());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getInteger());
            }
            if(key.getString().equals("owner")) {
                ByteStringStackItem byteStringStackItem = (ByteStringStackItem) mapStack.getMap().get(key);
                token.setOwner(Hash160.fromAddress(byteStringStackItem.getAddress()));
//                System.out.println(key.getString() +"---" +  Hash160.fromAddress(byteStringStackItem.getAddress()));
            }
            if(key.getString().equals("isOnsale")){
                token.setOnSale(mapStack.getMap().get(key).getBoolean());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getBoolean());
            }
            if(key.getString().equals("Genre")){
                token.setGenre(mapStack.getMap().get(key).getString());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getString());
            }
            if(key.getString().equals("Hp")){
                token.setHp(mapStack.getMap().get(key).getInteger());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getInteger());
            }
            if(key.getString().equals("Attack")){
                token.setAttack(mapStack.getMap().get(key).getInteger());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getInteger());
            }
            if(key.getString().equals("Defense")){
                token.setDefense(mapStack.getMap().get(key).getInteger());
//                System.out.println(key.getString() +"---" + mapStack.getMap().get(key).getInteger());
            }

        }
        return token;
    }

    public static ArrayList<String> showOnSaleItems() throws Throwable {
        String function = "showItems";
        checkConnection();
        NeoInvokeFunction response = new SmartContract(scriptHash, neow3j)
                .callInvokeFunction(function, Signer.calledByEntry(account.getScriptHash()));
//        System.out.println(response.getInvocationResult());
        ArrayList res = (ArrayList) response.getInvocationResult().getStack().get(0).getValue();
        ArrayList<String> OnSaleItemArrayList = new ArrayList<>();
        int count = res.size() - 1;
        while(res.get(count) instanceof ByteStringStackItem) {
            ByteStringStackItem rr = (ByteStringStackItem) res.get(count);
            if(tokenProperties(rr.getString()).isOnSale()){
                System.out.println("=======" +  tokenProperties(rr.getString()).isOnSale() + "=======");
                OnSaleItemArrayList.add(rr.getString());
               System.out.println(rr.getString());
            }
            count --;
        }
        System.out.println(OnSaleItemArrayList.size());
        return OnSaleItemArrayList;
    }

    public static void sellOnSaleItem(Hash160 to, String name) throws Throwable {
        String function = "transferItem";
        ContractParameter usrParam = ContractParameter.hash160(to);
        ContractParameter nameParam = ContractParameter.string(name);
        checkConnection();
        TransactionBuilder txBuilder = new SmartContract(scriptHash, neow3j)
                .invokeFunction(function, usrParam,nameParam);
        NeoSendRawTransaction response = txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .wallet(wallet)
                .sign()
                .send();
        System.out.println(txBuilder.signers(Signer.calledByEntry(account.getScriptHash()))
                .getScript().toString());
    }

    public Neow3j getNeow3j(){
        return neow3j;
    }

    public Wallet getWallet(){
        return  wallet;
    }

    public static Account getAccount(){
        return account;
    }


    public static void main(String[] args) throws Throwable {
        startConnection();
        importWallet(privateKeyWif);

        int num = 9;

        switch (num) {
            case 1:
                //createWallet();
                setPoint(2000);
                break;

            case 2:
                Thread.sleep(10000);
                pointsOf(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                System.out.println(account.getNep17Balances(neow3j));
                break;

            case 3:
                withdraw(12, "pants");
                Thread.sleep(10000);
                pointsOf(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                System.out.println(account.getNep17Balances(neow3j));

                withdraw(13, "hat");
                Thread.sleep(10000);
                pointsOf(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                System.out.println(account.getNep17Balances(neow3j));

                withdraw(15, "glove");
                Thread.sleep(10000);
                pointsOf(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                System.out.println(account.getNep17Balances(neow3j));

                withdraw(20, "socks");
                Thread.sleep(10000);
                pointsOf(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                System.out.println(account.getNep17Balances(neow3j));
                break;

            case 4:
                //查看用户获得的NFT的名字以及数量
                showMyItems(account.getScriptHash());
                banlanceOf(account.getScriptHash());
                break;

            case 5:
                //查看生成的NFT属性
                tokenProperties("pants");
                tokenProperties("hat");
                tokenProperties("glove");
                break;

            case 6:
                onSale(account.getScriptHash(), "hat");
                onSale(account.getScriptHash(), "pants");
                onSale(account.getScriptHash(), "socks");
                break;

            case 7:
                tokenProperties("hat");
                tokenProperties("pants");
                tokenProperties("glove");
                tokenProperties("socks");
                break;

            case 8:
                showOnSaleItems();
                break;

            case 9:
                sellOnSaleItem(Hash160.fromAddress("NdqhW7YhhMtgpjdFcqMTXggTP7JHjFFHdm"), "hat");
                break;

            case 10:
               
        }
    }

}


