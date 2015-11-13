import Foundation
import SwiftyJSON

public struct WithCustomTypesArray: Serializable, Equatable {
    
    public let ints: [CustomInt]?
    
    public let arrays: [[Simple]]?
    
    public let maps: [[String: Simple]]?
    
    public let unions: [WithCustomTypesArrayUnion]?
    
    public let fixed: [String]?
    
    public init(
        ints: [CustomInt]?,
        arrays: [[Simple]]?,
        maps: [[String: Simple]]?,
        unions: [WithCustomTypesArrayUnion]?,
        fixed: [String]?
    ) {
        self.ints = ints
        self.arrays = arrays
        self.maps = maps
        self.unions = unions
        self.fixed = fixed
    }
    
    public static func readJSON(json: JSON) throws -> WithCustomTypesArray {
        return WithCustomTypesArray(
            ints: try json["ints"].optional(.Array).array.map { try $0.map { try CustomIntCoercer.coerceInput(try $0.required(.Number).intValue) } },
            arrays: try json["arrays"].optional(.Array).array.map { try $0.map { try $0.required(.Array).arrayValue.map { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) } } },
            maps: try json["maps"].optional(.Array).array.map { try $0.map { try $0.required(.Dictionary).dictionaryValue.mapValues { try Simple.readJSON(try $0.required(.Dictionary).jsonValue) } } },
            unions: try json["unions"].optional(.Array).array.map { try $0.map { try WithCustomTypesArrayUnion.readJSON(try $0.required(.Dictionary).jsonValue) } },
            fixed: try json["fixed"].optional(.Array).array.map { try $0.map { try $0.required(.String).stringValue } }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let ints = self.ints {
            dict["ints"] = ints.map { CustomIntCoercer.coerceOutput($0) }
        }
        if let arrays = self.arrays {
            dict["arrays"] = arrays.map { $0.map { $0.writeData() } }
        }
        if let maps = self.maps {
            dict["maps"] = maps.map { $0.mapValues { $0.writeData() } }
        }
        if let unions = self.unions {
            dict["unions"] = unions.map { $0.writeData() }
        }
        if let fixed = self.fixed {
            dict["fixed"] = fixed.map { $0 }
        }
        return dict
    }
}
public func ==(lhs: WithCustomTypesArray, rhs: WithCustomTypesArray) -> Bool {
    return (
        (lhs.ints == nil ? (rhs.ints == nil) : lhs.ints! == rhs.ints!) &&
        (lhs.arrays == nil ? (rhs.arrays == nil) : lhs.arrays! == rhs.arrays!) &&
        (lhs.maps == nil ? (rhs.maps == nil) : lhs.maps! == rhs.maps!) &&
        (lhs.unions == nil ? (rhs.unions == nil) : lhs.unions! == rhs.unions!) &&
        (lhs.fixed == nil ? (rhs.fixed == nil) : lhs.fixed! == rhs.fixed!) &&
        true
    )
}
