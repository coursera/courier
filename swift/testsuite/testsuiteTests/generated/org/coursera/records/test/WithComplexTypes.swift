import Foundation
import SwiftyJSON

struct WithComplexTypes {
    
    let record: Simple?
    
    let `enum`: Fruits?
    
    let union: Union?
    
    let array: [Int]?
    
    let map: [String: Int]?
    
    let complexMap: [String: Simple]?
    
    let custom: Int?
    
    init(record: Simple?, `enum`: Fruits?, union: Union?, array: [Int]?, map: [String: Int]?, complexMap: [String: Simple]?, custom: Int?) {
        
        self.record = record
        
        self.`enum` = `enum`
        
        self.union = union
        
        self.array = array
        
        self.map = map
        
        self.complexMap = complexMap
        
        self.custom = custom
    }
    
    enum Union {
        
        case IntMember(Int)
        
        case StringMember(String)
        
        case SimpleMember(Simple)
        case UNKNOWN$([String : JSON])
        
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            
            if let member = dictionary["int"] {
                return .IntMember(member.intValue)
            }
            
            if let member = dictionary["string"] {
                return .StringMember(member.stringValue)
            }
            
            if let member = dictionary["org.coursera.records.test.Simple"] {
                return .SimpleMember(Simple.read(member.jsonValue))
            }
            return .UNKNOWN$(dictionary)
        }
        func write() -> [String : JSON] {
            switch self {
                
            case .IntMember(let member):
                return ["int": JSON(member)];
                
            case .StringMember(let member):
                return ["string": JSON(member)];
                
            case .SimpleMember(let member):
                return ["org.coursera.records.test.Simple": JSON(member.write())];
            case .UNKNOWN$(let dictionary):
                return dictionary
            }
        }
    }
    
    static func read(json: JSON) -> WithComplexTypes {
        return WithComplexTypes(
        record:
        json["record"].json.map { Simple.read($0) },
        `enum`:
        json["enum"].string.map { Fruits.read($0) },
        union:
        json["union"].json.map { Union.read($0) },
        array:
        json["array"].array.map { $0.map { $0.intValue } },
        map:
        json["map"].dictionary.map { $0.mapValues { $0.intValue } },
        complexMap:
        json["complexMap"].dictionary.map { $0.mapValues { Simple.read($0.jsonValue) } },
        custom:
        json["custom"].int
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let record = self.record {
            json["record"] = JSON(record.write())
        }
        
        if let `enum` = self.`enum` {
            json["enum"] = JSON(`enum`.write())
        }
        
        if let union = self.union {
            json["union"] = JSON(union.write())
        }
        
        if let array = self.array {
            json["array"] = JSON(array)
        }
        
        if let map = self.map {
            json["map"] = JSON(map)
        }
        
        if let complexMap = self.complexMap {
            json["complexMap"] = JSON(complexMap.mapValues { JSON($0.write()) })
        }
        
        if let custom = self.custom {
            json["custom"] = JSON(custom)
        }
        
        return json
    }
}

