using System.Collections;

namespace Neo.SmartContract{
    
    public class Users{

            public ulong StartTimeStamp;
            public  ulong EndTimeStamp ;
            public  long Interval = 0;
            public object[] History;
            public ulong Point = 0;
            public bool State = false;

        public Users(){}
        public Users(ulong startTimeStamp, ulong endTimeStamp, long interval, object[] history, ulong point, bool isEnd) {
                this.StartTimeStamp = startTimeStamp;
                this.EndTimeStamp = endTimeStamp;
                this.Interval = interval;
                this.History = history;
                this.Point = point;
                this.State = isEnd;
        }
    }

}