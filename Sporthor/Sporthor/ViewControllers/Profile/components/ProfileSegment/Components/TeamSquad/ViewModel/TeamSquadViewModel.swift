//
//  TeamSquadViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 20.03.2025.
//

import UIKit

final class TeamSquadViewModel: BaseSegmentViewModel {
    typealias DataModel = [TeamGroup]

    private var response: TeamMemberResponseModel?
    private var teamGroups: [TeamGroup] = []

    var numberOfItems: Int {
        return teamGroups.count
    }

    func fetch(userId: String?) async -> [TeamGroup]? {
        return await loadMockData(from: "teamSquad_mock")
    }

    func updateData(with data: Any?) {
        if let teamGroups = data as? [TeamGroup] {
            self.teamGroups = teamGroups
        }
    }

    private func loadMockData(from fileName: String) async -> [TeamGroup]? {
        guard let path = Bundle.main.path(forResource: fileName, ofType: "json") else {
            print("❌ Mock data file not found: \(fileName)")
            return nil
        }

        do {
            let data = try Data(contentsOf: URL(fileURLWithPath: path))
            let response = try JSONDecoder().decode(TeamMemberResponseModel.self, from: data)
            self.response = response
            self.teamGroups = response.teamMembers ?? []
            try? await Task.sleep(nanoseconds: 300_000_000)
            return response.teamMembers
        } catch {
            print("❌ JSON parse hatası: \(error)")
            return nil
        }
    }

    func teamSquad(at index: Int) -> TeamGroup? {
        return teamGroups[safe: index]
    }

    func cell(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: TeamSquadCell.reuseIdentifier,
            for: indexPath
        ) as? TeamSquadCell else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
        cell.configure(with: teamGroups[safe: indexPath.row])
        return cell
    }

    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize {
        return TeamSquadSizeCalculator(
            width: collectionView.frame.size.width,
            teamSquad: teamGroups[safe: indexPath.row]
        ).componentSize
    }
}
