//
//  CreateChatContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import UIKit

protocol CreateChatPresenterProtocol: BasePresenterProtocol {
    var view: CreateChatPresenterDelegate? { get set }
    var interactor: CreateChatInteractorProtocol { get set }
    var router: CreateChatRouterProtocol { get set }
    
    var isSearchActive: Bool { get }
    var hasNoSearchResults: Bool { get }
    
    func numberOfSections() -> Int
    func numberOfRows(in section: Int) -> Int
    func cellForRow(at indexPath: IndexPath, in tableView: UITableView) -> UITableViewCell
    func heightForRowAt(at indexPath: IndexPath, in tableView: UITableView) -> CGFloat
    func titleForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> String?
    func heightForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    func actionItem(at index: Int) -> ActionItemEnum?
    func search(text: String)
    func viewDidLoad()
    func didSelect(indexPath: IndexPath)
}

protocol CreateChatPresenterDelegate: BasePresenterDelegate {
    func reloadData()
}

protocol CreateChatInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreateChatInteractorDelegate? { get set }
    
    func fetchAllContacts() async
    func search(query: String)
    func createGroup(
        name: String,
        selectedUserId: String,
        groupImagePath: String?,
        userName: String
    ) async
}

protocol CreateChatInteractorDelegate: BaseInteractorDelegate {
    func didFetchAllContacts(_ contacts: [CreateChatUserModel])
    func didSearchResults(_ contacts: [CreateChatUserModel])
    func didCreateGroupSuccess(response: GroupChatResponseModel, userName: String)
}

protocol CreateChatRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreateChatRoutes)
}

enum CreateChatRoutes {
    case createGroup
    case createCommunity
    case connectContacts
    case chat(
        userId: String?,
        displayName: String?,
        image: String?,
        isGroup: Bool,
        isNewCreated: Bool,
        toUserId: String?
    )
}
