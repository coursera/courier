import Foundation
import SwiftyJSON

public struct Wrapper: Serializable {
    
    public let fortune: Fortune?
    
    public init(
        fortune: Fortune?
    ) {
        self.fortune = fortune
    }
    
    public static func readJSON(json: JSON) throws -> Wrapper {
        return Wrapper(
            fortune: try json["fortune"].optional(.Dictionary).json.map { try Fortune.readJSON($0) }
        )
    }
    public func writeData() -> [String: AnyObject] {
        var dict: [String : AnyObject] = [:]
        if let fortune = self.fortune {
            dict["fortune"] = fortune.writeData()
        }
        return dict
    }
}
