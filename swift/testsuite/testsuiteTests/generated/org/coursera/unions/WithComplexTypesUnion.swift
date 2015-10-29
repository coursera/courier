import Foundation
import SwiftyJSON

struct WithComplexTypesUnion {
    
    let union: Union?
    
    init(union: Union?) {
        
        self.union = union
    }
    
    enum Union {
        
        case EmptyMember(Empty)
        
        case FruitsMember(Fruits)
        
        case ArrayMember([Simple])
        
        case MapMember([String: Simple])
        
        case FixedMember(String)
        case UNKNOWN$([String : JSON])
        
        static func read(json: JSON) -> Union {
            let dictionary = json.dictionaryValue
            
            if let member = dictionary["org.coursera.records.test.Empty"] {
                return .EmptyMember(Empty.read(member.jsonValue))
            }
            
            if let member = dictionary["org.coursera.enums.Fruits"] {
                return .FruitsMember(Fruits.read(member.stringValue))
            }
            
            if let member = dictionary["array"] {
                return .ArrayMember(member.arrayValue.map { Simple.read($0.jsonValue) })
            }
            
            if let member = dictionary["map"] {
                return .MapMember(member.dictionaryValue.mapValues { Simple.read($0.jsonValue) })
            }
            
            if let member = dictionary["org.coursera.fixed.Fixed8"] {
                return .FixedMember(member.stringValue)
            }
            return .UNKNOWN$(dictionary)
        }
        func write() -> [String : JSON] {
            switch self {
                
            case .EmptyMember(let member):
                return ["org.coursera.records.test.Empty": JSON(member.write())];
                
            case .FruitsMember(let member):
                return ["org.coursera.enums.Fruits": JSON(member.write())];
                
            case .ArrayMember(let member):
                return ["array": JSON(member.map { JSON($0.write()) })];
                
            case .MapMember(let member):
                return ["map": JSON(member.mapValues { JSON($0.write()) })];
                
            case .FixedMember(let member):
                return ["org.coursera.fixed.Fixed8": JSON(member)];
            case .UNKNOWN$(let dictionary):
                return dictionary
            }
        }
    }
    
    static func read(json: JSON) -> WithComplexTypesUnion {
        return WithComplexTypesUnion(
        union:
        json["union"].json.map { Union.read($0) }
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let union = self.union {
            json["union"] = JSON(union.write())
        }
        
        return json
    }
}

