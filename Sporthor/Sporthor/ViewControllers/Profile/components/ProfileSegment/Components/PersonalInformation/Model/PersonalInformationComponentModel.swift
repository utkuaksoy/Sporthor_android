//
//  PersonalInformationComponentModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Foundation

final class PersonalInformationComponentModel: Decodable {
  private(set) var type: PersonalInformationComponentTypes?
  private(set) var typeId: Int?
  private(set) var id: String?
  var dataModel: Decodable?
  private var identifier = UUID()

  enum CodingKeys: String, CodingKey {
    case type, typeId, id
    case dataModel = "data"
  }

  // MARK: - Initializer

  required init(from decoder: Decoder) throws {
    let container = try decoder.container(keyedBy: CodingKeys.self)
    self.type = getTypeInfo(with: container)
    self.typeId = (try? container.decode(Int.self, forKey: .typeId)) ?? .zero
    self.id = (try? container.decode(String.self, forKey: .id)) ?? ""
    self.dataModel = try getDataModel(with: container, type: type)
  }

  init(
    dictionary: [String: Any?]?,
    type: PersonalInformationComponentTypes,
    dataModel: Codable? = nil,
    typeId: Int,
    id: String
  ) {
    self.type = type
    self.typeId = typeId
    self.dataModel = dataModel
    self.id = id
  }

  // MARK: - Private Methods

  private func getTypeInfo(with container: KeyedDecodingContainer<CodingKeys>) -> PersonalInformationComponentTypes? {
    (try? container.decode(PersonalInformationComponentTypes.self, forKey: .type))
  }

  private func getDataModel(
    with container: KeyedDecodingContainer<CodingKeys>,
    type: PersonalInformationComponentTypes?
  ) throws -> Decodable? {
    guard let type, let model = PersonalInformationComponentTypes.componentModelMap[type] else {
      return nil
    }
    return try container.decode(model, forKey: .dataModel)
  }
}

extension PersonalInformationComponentModel: Equatable {
  static func == (lhs: PersonalInformationComponentModel, rhs: PersonalInformationComponentModel) -> Bool {
    lhs.id == rhs.id && lhs.identifier == rhs.identifier
  }
}

extension PersonalInformationComponentModel: Hashable {
  func hash(into hasher: inout Hasher) {
    hasher.combine(id)
    hasher.combine(identifier)
  }
}
