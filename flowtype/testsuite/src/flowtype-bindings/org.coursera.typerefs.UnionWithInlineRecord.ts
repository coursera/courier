import { InlineRecord2 } from "./org.coursera.typerefs.InlineRecord2";
import { InlineRecord } from "./org.coursera.typerefs.InlineRecord";

export type UnionWithInlineRecord = UnionWithInlineRecord.InlineRecordMember | UnionWithInlineRecord.InlineRecord2Member;
export module UnionWithInlineRecord {
  export interface UnionWithInlineRecordMember {
    [key: string]: InlineRecord | InlineRecord2;
  }
  export interface InlineRecordMember extends UnionWithInlineRecordMember {
    "org.coursera.typerefs.InlineRecord": InlineRecord;
  }
  export interface InlineRecord2Member extends UnionWithInlineRecordMember {
    "org.coursera.typerefs.InlineRecord2": InlineRecord2;
  }
  export function unpack(union: UnionWithInlineRecord) {
    return {
      inlineRecord: union["org.coursera.typerefs.InlineRecord"] as InlineRecord,
      inlineRecord2: union["org.coursera.typerefs.InlineRecord2"] as InlineRecord2
    };
  }
}
