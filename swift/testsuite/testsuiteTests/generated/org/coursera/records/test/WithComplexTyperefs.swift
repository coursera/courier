import Foundation
import SwiftyJSON

struct WithComplexTyperefs {
    
    let `enum`: Fruits?
    
    let record: Empty?
    
    let map: [String: Empty]?
    
    let array: [Empty]?
    
    let union: UnionTyperef?
    
    init(`enum`: Fruits?, record: Empty?, map: [String: Empty]?, array: [Empty]?, union: UnionTyperef?) {
        
        self.`enum` = `enum`
        
        self.record = record
        
        self.map = map
        
        self.array = array
        
        self.union = union
    }
    
    static func read(json: JSON) -> WithComplexTyperefs {
        return WithComplexTyperefs(
        `enum`:
        json["enum"].string.map { Fruits.read($0) },
        record:
        json["record"].json.map { Empty.read($0) },
        map:
        json["map"].dictionary.map { $0.mapValues { Empty.read($0.jsonValue) } },
        array:
        json["array"].array.map { $0.map { Empty.read($0.jsonValue) } },
        union:
        json["union"].json.map { UnionTyperef.read($0) }
        )
    }
    func write() -> [String : JSON] {
        var json: [String : JSON] = [:]
        
        if let `enum` = self.`enum` {
            json["enum"] = JSON(`enum`.write())
        }
        
        if let record = self.record {
            json["record"] = JSON(record.write())
        }
        
        if let map = self.map {
            json["map"] = JSON(map.mapValues { JSON($0.write()) })
        }
        
        if let array = self.array {
            json["array"] = JSON(array.map { JSON($0.write()) })
        }
        
        if let union = self.union {
            json["union"] = JSON(union.write())
        }
        
        return json
    }
}
