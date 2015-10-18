import Foundation
import SwiftyJSON

struct WithPrimitivesMap: Equatable {
    
    let ints: [String: Int]?
    
    let longs: [String: Int]?
    
    let floats: [String: Float]?
    
    let doubles: [String: Double]?
    
    let booleans: [String: Bool]?
    
    let strings: [String: String]?
    
    let bytes: [String: String]?
    
    init(ints: [String: Int]?, longs: [String: Int]?, floats: [String: Float]?, doubles: [String: Double]?, booleans: [String: Bool]?, strings: [String: String]?, bytes: [String: String]?) {
        
        self.ints = ints
        
        self.longs = longs
        
        self.floats = floats
        
        self.doubles = doubles
        
        self.booleans = booleans
        
        self.strings = strings
        
        self.bytes = bytes
    }
    
    static func read(json: JSON) -> WithPrimitivesMap {
        return WithPrimitivesMap(
        ints: json["ints"].dictionary.map { $0.mapValues { $0.intValue } },
        longs: json["longs"].dictionary.map { $0.mapValues { $0.intValue } },
        floats: json["floats"].dictionary.map { $0.mapValues { $0.floatValue } },
        doubles: json["doubles"].dictionary.map { $0.mapValues { $0.doubleValue } },
        booleans: json["booleans"].dictionary.map { $0.mapValues { $0.boolValue } },
        strings: json["strings"].dictionary.map { $0.mapValues { $0.stringValue } },
        bytes: json["bytes"].dictionary.map { $0.mapValues { $0.stringValue } })
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
func ==(lhs: WithPrimitivesMap, rhs: WithPrimitivesMap) -> Bool {
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
