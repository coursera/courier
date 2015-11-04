//
//  MagicEightBallAnswer.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation
import SwiftyJSON

public enum MagicEightBallAnswer: Equatable {
    
    case IT_IS_CERTAIN
    
    /**
     * Where later is at least 10ms from now.
     */
    case ASK_AGAIN_LATER
    case OUTLOOK_NOT_SO_GOOD
    case UNKNOWN$(String)
    
    // TODO: can we avoid this?   Ideally we would do `MagicEightBallAnswer: String` but that prevents us from providing an UNKNOWN$ case.
    
    private struct Strings {
        static let IT_IS_CERTAIN = "IT_IS_CERTAIN"
        static let ASK_AGAIN_LATER = "ASK_AGAIN_LATER"
        static let OUTLOOK_NOT_SO_GOOD = "OUTLOOK_NOT_SO_GOOD"
    }
    
    public static func read(symbol: String) -> MagicEightBallAnswer {
        switch symbol {
        case MagicEightBallAnswer.Strings.IT_IS_CERTAIN:
            return .IT_IS_CERTAIN
        case MagicEightBallAnswer.Strings.ASK_AGAIN_LATER:
            return .ASK_AGAIN_LATER
        case MagicEightBallAnswer.Strings.OUTLOOK_NOT_SO_GOOD:
            return .OUTLOOK_NOT_SO_GOOD
        default:
            return .UNKNOWN$(symbol)
        }
    }
    
    public func write() -> String {
        switch self {
        case .IT_IS_CERTAIN:
            return MagicEightBallAnswer.Strings.IT_IS_CERTAIN
        case .ASK_AGAIN_LATER:
            return MagicEightBallAnswer.Strings.ASK_AGAIN_LATER
        case .OUTLOOK_NOT_SO_GOOD:
            return MagicEightBallAnswer.Strings.OUTLOOK_NOT_SO_GOOD
        case .UNKNOWN$(let symbol):
            return symbol
        }
    }
    
    /* TODO: figure out how to properly implement
    var hashValue: Int {
        return write().hashValue
    }
    */
}

public func ==(lhs: MagicEightBallAnswer, rhs: MagicEightBallAnswer) -> Bool {
    switch (lhs, rhs) {
    case (.IT_IS_CERTAIN, .IT_IS_CERTAIN):
        return true
    case (.ASK_AGAIN_LATER, .ASK_AGAIN_LATER):
        return true
    case (.OUTLOOK_NOT_SO_GOOD, .OUTLOOK_NOT_SO_GOOD):
        return true
    case (let .UNKNOWN$(lhs), let .UNKNOWN$(rhs)):
        return lhs == rhs
    default:
        return false
    }
}