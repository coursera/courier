import { CustomRecord } from "./org.coursera.customtypes.CustomRecord";

export interface WithRecordCustomTypeUnion {
  
  union : WithRecordCustomTypeUnion.Union;
}
export module WithRecordCustomTypeUnion {
  
  export type Union = Union.CustomRecordMember;
  export module Union {
    export interface UnionMember {
      [key: string]: CustomRecord;
    }
    export interface CustomRecordMember extends UnionMember {
      "org.coursera.records.test.Message": CustomRecord;
    }
    export function unpack(union: Union) {
      return {
        customRecord: union["org.coursera.records.test.Message"] as CustomRecord
      };
    }
  }
}
