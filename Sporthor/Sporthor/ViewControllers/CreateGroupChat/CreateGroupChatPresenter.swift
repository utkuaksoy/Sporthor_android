//
//  CreateGroupChatPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 27.03.2025.
//
//

import UIKit

final class CreateGroupChatPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreateGroupChatPresenterDelegate? {
        get { return self.baseView as? CreateGroupChatPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreateGroupChatInteractorProtocol {
        get { return self.baseInteractor as! CreateGroupChatInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreateGroupChatRouterProtocol {
        get { return self.baseRouter as! CreateGroupChatRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Private Properties
    private var allContacts: [CreateChatUserModel] = []
    private var filteredContacts: [CreateChatUserModel] = []
    private var selectedContacts: Set<String> = []
    private var isSearching: Bool = false
    private var groupName: String?
    private var groupImage: UIImage?
    private var groupImagePath: String?
    private var fromType: CreateGroupChatViewType = .newGroup
    
    // MARK: - Initialize
    init(view: CreateGroupChatPresenterDelegate,
         interactor: CreateGroupChatInteractorProtocol,
         router: CreateGroupChatRouterProtocol,
         fromType: CreateGroupChatViewType
    ) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.fromType = fromType
        self.interactor.delegate = self
    }
}

// MARK: - CreateGroupChatPresenterProtocol
extension CreateGroupChatPresenter: CreateGroupChatPresenterProtocol {
    
    var fromViewType: CreateGroupChatViewType {
        fromType
    }
    
    func viewDidLoad() {
        view?.configureHeaderView(type: fromType)
        Task {
            await interactor.fetchAllContacts()
        }
    }
    
    func uploadImage(with image: UIImage) {
        Task {
            await interactor.uploadImage(image)
        }
    }
    
    func numberOfSections() -> Int {
        return 1
    }
    
    func numberOfRows(in section: Int) -> Int {
        if isSearching {
            return filteredContacts.count
        }
        return allContacts.count
    }
    
    func cellForRow(at indexPath: IndexPath, in tableView: UITableView) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(
            withIdentifier: ChatUserCell.reuseIdentifier,
            for: indexPath
        ) as? ChatUserCell else {
            return UITableViewCell()
        }
        
        let contact = isSearching ? filteredContacts[indexPath.row] : allContacts[indexPath.row]
        cell.configure(
            name: contact.name,
            role: contact.summary,
            imageUrl: contact.imageUrl,
            isGroupSelection: true,
            isSelected: selectedContacts.contains(contact.id)
        )
        return cell
    }
    
    func heightForRowAt(at indexPath: IndexPath, in tableView: UITableView) -> CGFloat {
        return 72
    }
    
    func titleForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> String? {
        guard !isSearching else { return nil }
        return "Kişiler"
    }
    
    func heightForHeaderInSection(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        return fromType == .newGroup ? 33 : .zero
    }
    
    func search(text: String) {
        if text.isEmpty {
            isSearching = false
            view?.reloadData()
        } else {
            isSearching = true
            interactor.search(query: text)
        }
    }
    
    func didSelectRow(at indexPath: IndexPath) {
        let contact = isSearching ? filteredContacts[indexPath.row] : allContacts[indexPath.row]
        
        if selectedContacts.contains(contact.id) {
            selectedContacts.remove(contact.id)
        } else {
            selectedContacts.insert(contact.id)
        }
        
        view?.reloadData()
        view?.updateCreateGroupButton(isEnabled: selectedContacts.count > .zero)
    }
    
    func createGroupButtonTapped(with groupName: String) {
        let selectedUsers = allContacts
            .filter { selectedContacts.contains($0.id) }
        Task {
            await interactor.createGroup(
                name: groupName,
                selectedUsers: selectedUsers,
                groupImagePath: groupImagePath
            )
        }
    }
    
    func updateGroupButtonTapped(with groupId: String) {
        let selectedIds = allContacts
            .filter { selectedContacts.contains($0.id) }.map { $0.id }
        Task {
            await interactor.updateGroup(
                groupId: groupId,
                newUsers: selectedIds
            )
        }
    }
    
    private func navigate(_ routes: CreateGroupChatRoutes) {
        router.handleRouter(routes)
    }
}

// MARK: - CreateGroupChatInteractorDelegate
extension CreateGroupChatPresenter: CreateGroupChatInteractorDelegate {
    func didUploadSuccess(filePath: String) {
        let imageUrl = filePath
        groupImagePath = imageUrl
    }
    
    func didFetchAllContacts(_ contacts: [CreateChatUserModel]) {
        self.allContacts = contacts
        DispatchQueue.main.async {
            self.view?.reloadData()
        }
    }
    
    func didSearchResults(_ contacts: [CreateChatUserModel]) {
        self.filteredContacts = contacts
        DispatchQueue.main.async {
            self.view?.reloadData()
        }
    }
    
    func didCreateGroupSuccess(response: GroupChatResponseModel) {
        DispatchQueue.main.async { [weak self] in
            self?.router.handleRouter(
                .chat(
                    userId: response.id,
                    displayName: response.name,
                    image: response.image,
                    isGroup: true,
                    isNewCreated: true,
                    toUserId: response.toUserId
                )
            )
        }
    }
    
    func didUpdateGroupSuccess() {
        DispatchQueue.main.async {
            guard let navigationController = UIApplication.shared.activeNavigationController else { return }
            if let targetVC = navigationController.viewControllers.first(where: { $0 is MessagesViewController }) {
                navigationController.popToViewController(targetVC, animated: true)
            }
        }
    }
}
