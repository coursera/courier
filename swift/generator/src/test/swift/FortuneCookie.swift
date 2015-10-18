//
//  FortuneCookie.swift
//  Bindings
//
//  Created by David Le on 10/12/15.
//  Copyright © 2015 David Le. All rights reserved.
//

import Foundation

/**
    A fortune cookie.
*/
struct FortuneCookie {
   
    /**
        A fortune cookie message.
    */
    let message: String
    
    var certainty: Float?
    
    let luckyNumbers: [Int]
}