import { Note } from "./org.coursera.records.Note";
import { Message } from "./org.coursera.records.Message";

export type TypedDefinition = TypedDefinition.NoteMember | TypedDefinition.MessageMember;
export module TypedDefinition {
  export interface TypedDefinitionMember {
    [key: string]: Note | Message;
  }
  export interface NoteMember extends TypedDefinitionMember {
    "org.coursera.records.Note": Note;
  }
  export interface MessageMember extends TypedDefinitionMember {
    "org.coursera.records.Message": Message;
  }
  export function unpack(union: TypedDefinition) {
    return {
      note: union["org.coursera.records.Note"] as Note,
      message: union["org.coursera.records.Message"] as Message
    };
  }
}
