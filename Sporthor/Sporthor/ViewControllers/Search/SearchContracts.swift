//
//  SearchContracts.swift
//  Sporthor
//
//  Created by derTurke on 7.03.2025.
//
//

import UIKit

protocol SearchPresenterProtocol: BasePresenterProtocol {
    var view: SearchPresenterDelegate? { get set }
    var interactor: SearchInteractorProtocol { get set }
    var router: SearchRouterProtocol { get set }
    var searchState: SearchState { get set }
    var pastSearches: [SearchHistory] { get set }
    var searches: [SearchList] { get set }
    var discovers: [String] { get set }
    var searchText: String { get set }
    
    func viewDidLoad()
    func searchBegin()
    func search(_ text: String)
    func searchEnd(_ text: String)
    func searchCancel()
    func didSelectItemAt(_ indexPath: IndexPath)
    func deletePastItemAt(model: SearchHistory)
    func deleteAllPastItem()
}

protocol SearchPresenterDelegate: BasePresenterDelegate {
    func prepareNavigationBar()
    func prepareUI()
    func reloadData()
    func changeScrollDirection(_ scrollDirection: UICollectionView.ScrollDirection)
    func didChangeSearchBarText(_ text: String)
}

protocol SearchInteractorProtocol: BaseInteractorProtocol {
    var delegate: SearchInteractorDelegate? { get set }
    
    func search(_ request: [String: Any]) async
    func addSearchHistory(_ request: [String: Any]) async
    func getSearchHistory() async
    func removeSearchHistory(request: [String: Any]) async
}

protocol SearchInteractorDelegate: BaseInteractorDelegate {
    func didSearch(_ searchList: [SearchList])
    func didAddSearchHistory()
    func didGetSearchHistory(_ histories: [SearchHistory])
    func didRemoveSearchHistory()
}

protocol SearchRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: SearchRoutes)
}

enum SearchRoutes {
    case profile(id: String,
                 username: String)
}

// MARK: - SearchState Enum
enum SearchState {
    case past, search, discover
}

enum Searching: Int, CaseIterable {
    case placeholder = 0
    case searching
}

enum SearchType: String, CaseIterable {
    case user = "User"
    case team = "Team"
}
