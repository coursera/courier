import Foundation
import SwiftyJSON

public struct WithComplexTyperefs: JSONSerializable {
    
    public let `enum`: Fruits?
    
    public let record: Empty?
    
    public let map: [String: Empty]?
    
    public let array: [Empty]?
    
    public let union: UnionTyperef?
    
    public init(
        `enum`: Fruits?,
        record: Empty?,
        map: [String: Empty]?,
        array: [Empty]?,
        union: UnionTyperef?
    ) {
        self.`enum` = `enum`
        self.record = record
        self.map = map
        self.array = array
        self.union = union
    }
    
    public static func read(json: JSON) -> WithComplexTyperefs {
        return WithComplexTyperefs(
            `enum`: json["enum"].string.map { Fruits.read($0) },
            record: json["record"].json.map { Empty.read($0) },
            map: json["map"].dictionary.map { $0.mapValues { Empty.read($0.jsonValue) } },
            array: json["array"].array.map { $0.map { Empty.read($0.jsonValue) } },
            union: json["union"].json.map { UnionTyperef.read($0) }
        )
    }
    public func write() -> JSON {
        var json: [String : JSON] = [:]
        if let `enum` = self.`enum` {
            json["enum"] = JSON(`enum`.write())
        }
        if let record = self.record {
            json["record"] = record.write()
        }
        if let map = self.map {
            json["map"] = JSON(map.mapValues { $0.write() })
        }
        if let array = self.array {
            json["array"] = JSON(array.map { $0.write() })
        }
        if let union = self.union {
            json["union"] = union.write()
        }
        return JSON(json)
    }
}
