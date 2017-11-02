import { Note } from "./org.coursera.records.Note";
import { Message } from "./org.coursera.records.Message";

export type Union = Union.NoteMember | Union.MessageMember;
export module Union {
  export interface UnionMember {
    [key: string]: Note | Message;
  }
  export interface NoteMember extends UnionMember {
    "org.coursera.records.Note": Note;
  }
  export interface MessageMember extends UnionMember {
    "org.coursera.records.Message": Message;
  }
  export function unpack(union: Union) {
    return {
      note: union["org.coursera.records.Note"] as Note,
      message: union["org.coursera.records.Message"] as Message
    };
  }
}
