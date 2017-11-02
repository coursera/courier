import { Note } from "./org.coursera.records.Note";
import { Message } from "./org.coursera.records.Message";

export type FlatTypedDefinition = FlatTypedDefinition.NoteMember | FlatTypedDefinition.MessageMember;
export module FlatTypedDefinition {
  export interface FlatTypedDefinitionMember {
    [key: string]: Note | Message;
  }
  export interface NoteMember extends FlatTypedDefinitionMember {
    "org.coursera.records.Note": Note;
  }
  export interface MessageMember extends FlatTypedDefinitionMember {
    "org.coursera.records.Message": Message;
  }
  export function unpack(union: FlatTypedDefinition) {
    return {
      note: union["org.coursera.records.Note"] as Note,
      message: union["org.coursera.records.Message"] as Message
    };
  }
}
