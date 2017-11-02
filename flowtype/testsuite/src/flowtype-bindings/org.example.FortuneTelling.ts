import { MagicEightBall } from "./org.example.MagicEightBall";
import { FortuneCookie } from "./org.example.FortuneCookie";

export type FortuneTelling = FortuneTelling.FortuneCookieMember | FortuneTelling.MagicEightBallMember | FortuneTelling.StringMember;
export module FortuneTelling {
  export interface FortuneTellingMember {
    [key: string]: FortuneCookie | MagicEightBall | string;
  }
  export interface FortuneCookieMember extends FortuneTellingMember {
    "org.example.FortuneCookie": FortuneCookie;
  }
  export interface MagicEightBallMember extends FortuneTellingMember {
    "org.example.MagicEightBall": MagicEightBall;
  }
  export interface StringMember extends FortuneTellingMember {
    "string": string;
  }
  export function unpack(union: FortuneTelling) {
    return {
      fortuneCookie: union["org.example.FortuneCookie"] as FortuneCookie,
      magicEightBall: union["org.example.MagicEightBall"] as MagicEightBall,
      string$: union["string"] as string
    };
  }
}
