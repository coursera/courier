import Foundation
import SwiftyJSON

struct WithPrimitivesArray: Equatable {
    
    let ints: [Int]?
    
    let longs: [Int]?
    
    let floats: [Float]?
    
    let doubles: [Double]?
    
    let booleans: [Bool]?
    
    let strings: [String]?
    
    let bytes: [String]?
    
    init(ints: [Int]?, longs: [Int]?, floats: [Float]?, doubles: [Double]?, booleans: [Bool]?, strings: [String]?, bytes: [String]?) {
        
        self.ints = ints
        
        self.longs = longs
        
        self.floats = floats
        
        self.doubles = doubles
        
        self.booleans = booleans
        
        self.strings = strings
        
        self.bytes = bytes
    }
    
    static func read(json: JSON) -> WithPrimitivesArray {
        return WithPrimitivesArray(
        ints: json["ints"].array.map { $0.map { $0.intValue } },
        longs: json["longs"].array.map { $0.map { $0.intValue } },
        floats: json["floats"].array.map { $0.map { $0.floatValue } },
        doubles: json["doubles"].array.map { $0.map { $0.doubleValue } },
        booleans: json["booleans"].array.map { $0.map { $0.boolValue } },
        strings: json["strings"].array.map { $0.map { $0.stringValue } },
        bytes: json["bytes"].array.map { $0.map { $0.stringValue } })
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        if let ints = self.ints {
            json["ints"] = JSON(ints)
        }
        if let longs = self.longs {
            json["longs"] = JSON(longs)
        }
        if let floats = self.floats {
            json["floats"] = JSON(floats)
        }
        if let doubles = self.doubles {
            json["doubles"] = JSON(doubles)
        }
        if let booleans = self.booleans {
            json["booleans"] = JSON(booleans)
        }
        if let strings = self.strings {
            json["strings"] = JSON(strings)
        }
        if let bytes = self.bytes {
            json["bytes"] = JSON(bytes)
        }
        
        return json
    }
}
func ==(lhs: WithPrimitivesArray, rhs: WithPrimitivesArray) -> Bool {
    return (
    
    (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
    
    (lhs.longs == nil ? (rhs.longs == nil) : lhs.longs! == rhs.longs!) &&
    
    (lhs.floats == nil ? (rhs.floats == nil) : lhs.floats! == rhs.floats!) &&
    
    (lhs.doubles == nil ? (rhs.doubles == nil) : lhs.doubles! == rhs.doubles!) &&
    
    (lhs.booleans == nil ? (rhs.booleans == nil) : lhs.booleans! == rhs.booleans!) &&
    
    (lhs.strings == nil ? (rhs.strings == nil) : lhs.strings! == rhs.strings!) &&
    
    (lhs.bytes == nil ? (rhs.bytes == nil) : lhs.bytes! == rhs.bytes!) &&
    true)
}
