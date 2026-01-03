//
//  MatchesViewModel.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 16.03.2025.
//

import UIKit

final class MatchesViewModel: BaseSegmentViewModel {
    private var matches: [MatchModel] = []

    var numberOfItems: Int {
        return matches.count
    }

    func fetch(userId: String?) async -> [MatchModel]? {
        guard let path = Bundle.main.path(forResource: "matches_mock", ofType: "json") else {
            return nil
        }
        
        do {
            let data = try Data(contentsOf: URL(fileURLWithPath: path))
            let response = try JSONDecoder().decode(MatchesResponseModel.self, from: data)
            if let matches = response.matches {
                self.matches = matches
            }
            try? await Task.sleep(nanoseconds: 300_000_000)
            return response.matches
        } catch {
            print("❌ JSON parse hatası: \(error)")
            return nil
        }
    }

    func updateData(with data: Any?) {
        if let matches = data as? [MatchModel] {
            self.matches = matches
        }
    }

    func cell(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(
            withReuseIdentifier: MatchesCell.reuseIdentifier,
            for: indexPath
        ) as? MatchesCell else { return collectionView.dequeueEmptyReusableCell(with: indexPath) }
        cell.configure(with: matches[safe: indexPath.row])
        return cell
    }

    func size(_ collectionView: UICollectionView, at indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.size.width, height: 182)
    }

    func sectionHeader(in collectionView: UICollectionView, at indexPath: IndexPath, delegate: AnyObject?) -> UICollectionReusableView {
        return UICollectionReusableView()
    }
}
