//
//  SearchViewController.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import UIKit
import ComponentKit

final class SearchViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: SearchPresenterProtocol {
        get { return self.basePresenter as! SearchPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Element
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .vertical
        let collectionView = UICollectionView(frame: .zero,
                                              collectionViewLayout: layout)
        collectionView.delegate = self
        collectionView.dataSource = self
        collectionView.translatesAutoresizingMaskIntoConstraints = false
        collectionView.backgroundColor = .clear
        collectionView.showsHorizontalScrollIndicator = false
        collectionView.showsVerticalScrollIndicator = false
        return collectionView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        navigationController?.setNavigationBarHidden(false, animated: false)
        let appearance = UINavigationBarAppearance()
        appearance.configureWithOpaqueBackground()
        appearance.backgroundColor = .white
        appearance.shadowColor = .clear
        appearance.shadowImage = nil
        
        navigationController?.navigationBar.standardAppearance = appearance
        navigationController?.navigationBar.scrollEdgeAppearance = appearance
        navigationController?.navigationBar.compactAppearance = appearance
    }
    
    // MARK: - Custom Methods
}

// MARK: - SearchPresenterDelegate
extension SearchViewController: SearchPresenterDelegate {
    func prepareNavigationBar() {
        if let nav = navigationController as? CustomNavigationController {
            nav.customDelegate = self
            nav.searchBarTitleViewPlaceholder = "Sporthor'da Ara"
        }
    }
    
    func prepareUI() {
        view.addSubview(collectionView)
        
        NSLayoutConstraint.activate([
            collectionView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 8),
            collectionView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            collectionView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            collectionView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.collectionView.reloadData()
        }
    }
    
    func changeScrollDirection(_ scrollDirection: UICollectionView.ScrollDirection) {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            if let layout = self.collectionView.collectionViewLayout as? UICollectionViewFlowLayout {
                layout.scrollDirection = scrollDirection
                self.collectionView.setCollectionViewLayout(layout, animated: true)
                self.reloadData()
            }
        }
    }
    
    func didChangeSearchBarText(_ text: String) {
        if let nav = navigationController as? CustomNavigationController {
            nav.searchBarChangeText(text)
        }
    }
}

extension SearchViewController: UICollectionViewDataSource {
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        switch presenter.searchState {
        case .past:
            return presenter.pastSearches.count
        case .search:
            return 2
        case .discover:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        switch presenter.searchState {
        case .search:
            switch section {
            case 0:
                return 1
            case 1:
                return presenter.searches.count
            default:
                return 0
            }
        case .discover:
            return presenter.discovers.count
        default:
            return 1
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        switch presenter.searchState {
        case .past:
            let cell = SearchingCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bindPastSearch(delegate: self,
                                model: presenter.pastSearches[indexPath.section])
            return cell
        case .search:
            switch indexPath.section {
            case 0:
                let cell = SearchingCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.searchingPlaceholder(searchText: presenter.searchText)
                return cell
            case 1:
                let cell = SearchingCollectionViewCell.dequeue(from: collectionView, at: indexPath)
                cell.bind(model: presenter.searches[indexPath.row])
                return cell
            default:
                return UICollectionViewCell.dequeue(from: collectionView, at: indexPath)
            }
        case .discover:
            let cell = ImageViewCollectionViewCell.dequeue(from: collectionView, at: indexPath)
            cell.bind(image: presenter.discovers[indexPath.item])
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, viewForSupplementaryElementOfKind kind: String, at indexPath: IndexPath) -> UICollectionReusableView {
        if kind == UICollectionView.elementKindSectionHeader {
            switch presenter.searchState {
            case .past:
                let headerView = HeaderReusableView.dequeueReusableSupplementaryView(from: collectionView, ofKind: UICollectionView.elementKindSectionHeader, at: indexPath)
                headerView.bind(delegate: self, title: "Son Aramalar", detail: "Tümünü Temizle")
                return headerView
            default:
                return UICollectionReusableView()
            }
        }

        return UICollectionReusableView()
    }
}

extension SearchViewController: UICollectionViewDelegate {
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        presenter.didSelectItemAt(indexPath)
    }
}

extension SearchViewController: UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        switch presenter.searchState {
        case .past, .search:
            return CGSize(width: collectionView.frame.size.width - 32, height: 72)
        case .discover:
            return CGSize(width: (collectionView.frame.size.width - 6) / 3, height: 161)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumLineSpacingForSectionAt section: Int) -> CGFloat {
        switch presenter.searchState {
        case .discover:
            return 3
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, minimumInteritemSpacingForSectionAt section: Int) -> CGFloat {
        switch presenter.searchState {
        case .discover:
            return 3
        default:
            return .zero
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, insetForSectionAt section: Int) -> UIEdgeInsets {
        return .zero
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, referenceSizeForHeaderInSection section: Int) -> CGSize {
        switch presenter.searchState {
        case .past:
            return section == 0 ? CGSize(width: collectionView.bounds.width, height: 40) : .zero
        default:
            return .zero
        }
    }
}

// MARK: - CustomNavigationControllerDelegate
extension SearchViewController: CustomNavigationControllerDelegate {
    func navigationBarSearchBarDidBeginEditing(_ searchBar: CKSearchBar) {
        presenter.searchBegin()
    }
    
    func navigationBarSearchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.search(text)
    }
    
    func navigationBarSearchBarDidCancel(_ searchBar: CKSearchBar) {
        presenter.searchCancel()
    }
    
    func navigationBarSearchBarTextDidEndEditing(_ searchBar: CKSearchBar, text: String) {
        presenter.searchEnd(text)
    }
}

extension SearchViewController: SearchingCollectionViewCellDelegate {
    func deletePastSearchItem(with model: SearchHistory) {
        presenter.deletePastItemAt(model: model)
    }
}

// MARK: - HeaderReusableViewDelegate
extension SearchViewController: HeaderReusableViewDelegate {
    func didTappedDetail() {
        presenter.deleteAllPastItem()
    }
}
