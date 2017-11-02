import { CustomUnionTestId } from "./org.coursera.customtypes.CustomUnionTestId";

export interface WithCustomUnionTestId {
  
  union : WithCustomUnionTestId.Union;
}
export module WithCustomUnionTestId {
  
  export type Union = Union.CustomUnionTestIdMember;
  export module Union {
    export interface UnionMember {
      [key: string]: CustomUnionTestId;
    }
    export interface CustomUnionTestIdMember extends UnionMember {
      "int": CustomUnionTestId;
    }
    export function unpack(union: Union) {
      return {
        customUnionTestId: union["int"] as CustomUnionTestId
      };
    }
  }
}
