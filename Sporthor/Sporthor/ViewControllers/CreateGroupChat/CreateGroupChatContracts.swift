//
//  CreateGroupChatContracts.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import UIKit

protocol CreateGroupChatPresenterProtocol: BasePresenterProtocol {
    var view: CreateGroupChatPresenterDelegate? { get set }
    var interactor: CreateGroupChatInteractorProtocol { get set }
    var router: CreateGroupChatRouterProtocol { get set }
    var fromViewType: CreateGroupChatViewType { get }
    
    func viewDidLoad()
    func numberOfSections() -> Int
    func numberOfRows(in section: Int) -> Int
    func cellForRow(at indexPath: IndexPath, in tableView: UITableView) -> UITableViewCell
    func heightForRowAt(at indexPath: IndexPath, in tableView: UITableView) -> CGFloat
    func titleForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> String?
    func heightForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat
    func search(text: String)
    func didSelectRow(at indexPath: IndexPath)
    func createGroupButtonTapped(with groupName: String)
    func updateGroupButtonTapped(with groupId: String)
    func uploadImage(with image: UIImage)
}

protocol CreateGroupChatPresenterDelegate: BasePresenterDelegate {
    func reloadData()
    func updateCreateGroupButton(isEnabled: Bool)
    func showError(_ message: String)
    func configureHeaderView(type: CreateGroupChatViewType)
}

protocol CreateGroupChatInteractorProtocol: BaseInteractorProtocol {
    var delegate: CreateGroupChatInteractorDelegate? { get set }
    
    func fetchAllContacts() async
    func uploadImage(_ image: UIImage) async
    func search(query: String)
    func createGroup(
        name: String,
        selectedUsers: [CreateChatUserModel],
        groupImagePath: String?
    ) async
    func updateGroup(groupId: String, newUsers: [String]) async
}

protocol CreateGroupChatInteractorDelegate: BaseInteractorDelegate {
    func didFetchAllContacts(_ contacts: [CreateChatUserModel])
    func didSearchResults(_ contacts: [CreateChatUserModel])
    func didCreateGroupSuccess(response: GroupChatResponseModel)
    func didUploadSuccess(filePath: String)
    func didUpdateGroupSuccess()
}

protocol CreateGroupChatRouterProtocol: BaseRouterProtocol {
    func handleRouter(_ router: CreateGroupChatRoutes)
}

enum CreateGroupChatRoutes {
    case chat(
        userId: String?,
        displayName: String?,
        image: String?,
        isGroup: Bool,
        isNewCreated: Bool,
        toUserId: String?
    )
}
