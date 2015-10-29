import Foundation
import SwiftyJSON

struct WithTypedKeyMap: Equatable {
    
    let ints: [String: String]?
    
    let longs: [String: String]?
    
    let floats: [String: String]?
    
    let doubles: [String: String]?
    
    let booleans: [String: String]?
    
    let strings: [String: String]?
    
    let bytes: [String: String]?
    
    let record: [String: String]?
    
    let array: [String: String]?
    
    let `enum`: [String: String]?
    
    let custom: [String: String]?
    
    let fixed: [String: String]?
    
    let inlineRecord: [String: String]?
    
    init(ints: [String: String]?, longs: [String: String]?, floats: [String: String]?, doubles: [String: String]?, booleans: [String: String]?, strings: [String: String]?, bytes: [String: String]?, record: [String: String]?, array: [String: String]?, `enum`: [String: String]?, custom: [String: String]?, fixed: [String: String]?, inlineRecord: [String: String]?) {
        
        self.ints = ints
        
        self.longs = longs
        
        self.floats = floats
        
        self.doubles = doubles
        
        self.booleans = booleans
        
        self.strings = strings
        
        self.bytes = bytes
        
        self.record = record
        
        self.array = array
        
        self.`enum` = `enum`
        
        self.custom = custom
        
        self.fixed = fixed
        
        self.inlineRecord = inlineRecord
    }
    
    static func read(json: JSON) -> WithTypedKeyMap {
        return WithTypedKeyMap(
        ints:
        json["ints"].dictionary.map { $0.mapValues { $0.stringValue } },
        longs:
        json["longs"].dictionary.map { $0.mapValues { $0.stringValue } },
        floats:
        json["floats"].dictionary.map { $0.mapValues { $0.stringValue } },
        doubles:
        json["doubles"].dictionary.map { $0.mapValues { $0.stringValue } },
        booleans:
        json["booleans"].dictionary.map { $0.mapValues { $0.stringValue } },
        strings:
        json["strings"].dictionary.map { $0.mapValues { $0.stringValue } },
        bytes:
        json["bytes"].dictionary.map { $0.mapValues { $0.stringValue } },
        record:
        json["record"].dictionary.map { $0.mapValues { $0.stringValue } },
        array:
        json["array"].dictionary.map { $0.mapValues { $0.stringValue } },
        `enum`:
        json["enum"].dictionary.map { $0.mapValues { $0.stringValue } },
        custom:
        json["custom"].dictionary.map { $0.mapValues { $0.stringValue } },
        fixed:
        json["fixed"].dictionary.map { $0.mapValues { $0.stringValue } },
        inlineRecord:
        json["inlineRecord"].dictionary.map { $0.mapValues { $0.stringValue } }
        )
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
        
        if let record = self.record {
            json["record"] = JSON(record)
        }
        
        if let array = self.array {
            json["array"] = JSON(array)
        }
        
        if let `enum` = self.`enum` {
            json["enum"] = JSON(`enum`)
        }
        
        if let custom = self.custom {
            json["custom"] = JSON(custom)
        }
        
        if let fixed = self.fixed {
            json["fixed"] = JSON(fixed)
        }
        
        if let inlineRecord = self.inlineRecord {
            json["inlineRecord"] = JSON(inlineRecord)
        }
        
        return json
    }
}
func ==(lhs: WithTypedKeyMap, rhs: WithTypedKeyMap) -> Bool {
    return (
    
    (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
    
    (lhs.longs == nil ? (rhs.longs == nil) : lhs.longs! == rhs.longs!) &&
    
    (lhs.floats == nil ? (rhs.floats == nil) : lhs.floats! == rhs.floats!) &&
    
    (lhs.doubles == nil ? (rhs.doubles == nil) : lhs.doubles! == rhs.doubles!) &&
    
    (lhs.booleans == nil ? (rhs.booleans == nil) : lhs.booleans! == rhs.booleans!) &&
    
    (lhs.strings == nil ? (rhs.strings == nil) : lhs.strings! == rhs.strings!) &&
    
    (lhs.bytes == nil ? (rhs.bytes == nil) : lhs.bytes! == rhs.bytes!) &&
    
    (lhs.record == nil ? (rhs.record == nil) : lhs.record! == rhs.record!) &&
    
    (lhs.array == nil ? (rhs.array == nil) : lhs.array! == rhs.array!) &&
    
    (lhs.`enum` == nil ? (rhs.`enum` == nil) : lhs.`enum`! == rhs.`enum`!) &&
    
    (lhs.custom == nil ? (rhs.custom == nil) : lhs.custom! == rhs.custom!) &&
    
    (lhs.fixed == nil ? (rhs.fixed == nil) : lhs.fixed! == rhs.fixed!) &&
    
    (lhs.inlineRecord == nil ? (rhs.inlineRecord == nil) : lhs.inlineRecord! == rhs.inlineRecord!) &&
    true)
}
