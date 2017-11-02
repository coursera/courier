import { Map } from "./CourierRuntime";

export interface WithAnonymousUnionArray {
  
  unionsArray : Array<WithAnonymousUnionArray.UnionsArray>;
  
  unionsMap : Map<WithAnonymousUnionArray.UnionsMap>;
}
export module WithAnonymousUnionArray {
  
  export type UnionsArray = UnionsArray.IntMember | UnionsArray.StringMember;
  export module UnionsArray {
    export interface UnionsArrayMember {
      [key: string]: number | string;
    }
    export interface IntMember extends UnionsArrayMember {
      "int": number;
    }
    export interface StringMember extends UnionsArrayMember {
      "string": string;
    }
    export function unpack(union: UnionsArray) {
      return {
        int: union["int"] as number,
        string$: union["string"] as string
      };
    }
  }
  
  export type UnionsMap = UnionsMap.StringMember | UnionsMap.IntMember;
  export module UnionsMap {
    export interface UnionsMapMember {
      [key: string]: string | number;
    }
    export interface StringMember extends UnionsMapMember {
      "string": string;
    }
    export interface IntMember extends UnionsMapMember {
      "int": number;
    }
    export function unpack(union: UnionsMap) {
      return {
        string$: union["string"] as string,
        int: union["int"] as number
      };
    }
  }
}
