//
//  ProfileComponentsContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 11.03.2025.
//

import Foundation
import ComponentBaseKit
import ModelParsers

public struct ProfileComponentsContracts {
    public typealias CollectionDecodable = ComponentsDecodable<Components, ComponentCodingKey>
    
    public struct Components: CollectionComponentGroup {
        
        public typealias ViewModelDelegates = ProfileAboutViewModelDelegate &
        NextMatchesViewModelDelegate &
        ProfileInfoViewModelDelegate &
        ProfileActionButtonsViewModelDelegate &
        ProfileSegmentViewModelDelegate &
        TeamsViewModelDelegate &
        EmptyViewViewModelDelegate
        
        public typealias DisplayerDelegates = ProfileInfoCellDelegate &
        TeamsContainerCellDelegate &
        ProfileActionButtonsCellDelegate &
        PostImagesCellDelegate &
        EmptyViewCellDelegate
        
                
        public static let collectionSources: [String: any CollectionComponent.Type] = [
            "ProfileInfo": ProfileInfoComponent.self,
            "Teams": TeamsComponent.self,
            "ProfileAbout": ProfileAboutComponent.self,
            "ProfileActionButtons": ProfileActionButtonsComponent.self,
            "NextMatches": NextMatchesComponent.self,
            "Segments": ProfileSegmentComponent.self,
            "EmptyView": EmptyViewComponent.self,
        ]
    }
    
    public enum ComponentCodingKey: ComponentDecoderKeyType {
        public static var kindKey: ComponentCodingKey = .type
        
        case type
    }
}

public struct EmptyModel: Decodable {
    static let empty: Self = .init()
}
