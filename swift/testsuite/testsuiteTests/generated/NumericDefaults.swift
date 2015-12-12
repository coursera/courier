import Foundation
import SwiftyJSON

public struct NumericDefaults: Serializable {
    
    public let i: Int?
    
    public let l: Int?
    
    public let f: Float?
    
    public let d: Double?
    
    public init(
        i: Int? = 2147483647,
        l: Int? = 9223372036854775807,
        f: Float? = 3.4028233E38,
        d: Double? = 1.7976931348623157E308
    ) {
        self.i = i
        self.l = l
        self.f = f
        self.d = d
    }
    
    public static func readJSON(json: JSON) throws -> NumericDefaults {
        return NumericDefaults(
            i: try json["i"].optional(.Number).int,
            l: try json["l"].optional(.Number).int,
            f: try json["f"].optional(.Number).float,
            d: try json["d"].optional(.Number).double
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let i = self.i {
            dict["i"] = i
        }
        if let l = self.l {
            dict["l"] = l
        }
        if let f = self.f {
            dict["f"] = f
        }
        if let d = self.d {
            dict["d"] = d
        }
        return dict
    }
}
