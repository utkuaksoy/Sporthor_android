//
//  PersonalInfoViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import Factory
import NetworkKit
import UIKit

final class PersonalInfoViewModel: BaseSegmentViewModel {
    
    // MARK: - Private Properties
    
    @LazyInjected(\.networkManager) private var networkManager
    
    private var personalInfoComponents: [PersonalInformationComponentModel] = []

    var numberOfItems: Int {
        return personalInfoComponents.count
    }

    func fetch(userId: String?) async -> [PersonalInformationComponentModel]? {
        guard let networkManager else { return nil }
        
        let request = ProfileService.getProfileDetail(userId: userId)
        let result = await networkManager.request(
            service: request,
            responseType: PersonalInformationResponseModel.self
        )
        switch result {
        case .success(let response):
            self.personalInfoComponents = response.components
            return response.components
        case .failure(_):
            return nil
        }
    }

    func updateData(with data: Any?) {
        if let personalInfoComponents = data as? [PersonalInformationComponentModel] {
            self.personalInfoComponents = personalInfoComponents
        }
    }

    func cell(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionViewCell {
        guard let component = personalInfoComponents[safe: indexPath.row] else {
            return UICollectionViewCell()
        }
        switch component.type {
        case .profileCard:
            guard let cell = collectionView.dequeueReusableCell(
                withReuseIdentifier: PersonalInformationCell.reuseIdentifier,
                for: indexPath
            ) as? PersonalInformationCell,
                  let dataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? PersonaInfoDataModel
            else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
            cell.configure(with: dataModel)
            return cell
        case .currentTeams:
            guard let cell = collectionView.dequeueReusableCell(
                withReuseIdentifier: CurrentTeamsContainerCell.reuseIdentifier,
                for: indexPath
            ) as? CurrentTeamsContainerCell,
                  let dataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? TeamsDataModel
            else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
            cell.configure(with: dataModel, at: indexPath)
            return cell
        case .featuredSkills:
            guard let cell = collectionView.dequeueReusableCell(
                withReuseIdentifier: FeaturedSkillsCell.reuseIdentifier,
                for: indexPath
            ) as? FeaturedSkillsCell,
                  let dataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? FeaturedSkillsDataModel
            else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
            cell.configure(with: dataModel)
            return cell
        case .tournaments:
            guard let cell = collectionView.dequeueReusableCell(
                withReuseIdentifier: TournamentsContainerCell.reuseIdentifier,
                for: indexPath
            ) as? TournamentsContainerCell,
                  let dataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? TournamentDataModel
            else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
            cell.configure(with: dataModel, at: indexPath)
            return cell
        case .careerHistory:
            guard let cell = collectionView.dequeueReusableCell(
                withReuseIdentifier: CareerHistoryContainerCell.reuseIdentifier,
                for: indexPath
            ) as? CareerHistoryContainerCell,
                  let dataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? CareerHistoryDataModel
            else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
            cell.configure(with: dataModel)
            return cell
        default:
            return collectionView.dequeueEmptyReusableCell(with: indexPath)
        }
    }

    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize {
        guard let component = personalInfoComponents[safe: indexPath.row] else {
            return .zero
        }
        switch component.type {
        case .profileCard:
            return CGSize(width: collectionView.frame.size.width, height: 230)
        case .currentTeams:
            return CGSize(width: collectionView.frame.size.width, height: 100)
        case .featuredSkills:
            return CGSize(width: collectionView.frame.size.width, height: 250)
        case .tournaments:
            guard let tournamentDataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? TournamentDataModel,
                  let tournaments = tournamentDataModel.tournaments,
                  !tournaments.isEmpty
            else { return .zero }
            return TournamentsCalculator(width: collectionView.frame.size.width, tournaments: tournaments).componentSize
        case .careerHistory:
            guard let careerDataModel = personalInfoComponents[safe: indexPath.row]?.dataModel as? CareerHistoryDataModel,
                  let items = careerDataModel.items,
                  !items.isEmpty
            else { return .zero }
            return CareerHistoryCalculator(width: collectionView.frame.size.width, items: items).componentSize
        default:
            return .zero
        }
    }
}
