//
//  CreateChatPresenter.swift
//  Sporthor
//
//  Created by Mesut Canbaz on 26.03.2025.
//
//

import UIKit

enum CreateChatSection: Int, CaseIterable {
    case actions = 0
    case contracts = 1
}

enum ActionItemEnum: Int, CaseIterable {
    case group = 0
//    case community
//    case connectContracts
    
    var title: String {
        switch self {
        case .group:
            return "Grup Sohbeti Oluştur"
//        case .community:
//            return "Topluluk oluştur"
//        case .connectContracts:
//            return "Kişilerini bağla"
        }
    }
    
    var image: UIImage {
        switch self {
        case .group:
            return Asset.groupIcon.image
//        case .community:
//            return Asset.createCommunityIcon.image
//        case .connectContracts:
//            return Asset.connectContractsIcon.image
        }
    }
    
    var isBadgeHidden: Bool {
        switch self {
        case .group:
            return true
//        case .community:
//            return true
//        case .connectContracts:
//            return false
        }
    }
}


final class CreateChatPresenter: BasePresenter {
    // MARK: - VIPER Variables
    weak var view: CreateChatPresenterDelegate? {
        get { return self.baseView as? CreateChatPresenterDelegate }
        set { self.baseView = newValue }
    }
    
    var interactor: CreateChatInteractorProtocol {
        get { return self.baseInteractor as! CreateChatInteractorProtocol }
        set { self.baseInteractor = newValue }
    }
    
    var router: CreateChatRouterProtocol {
        get { return self.baseRouter as! CreateChatRouterProtocol }
        set { self.baseRouter = newValue }
    }
    
    // MARK: - Private Properties

    private var allContacts: [CreateChatUserModel] = []
    private var filteredContacts: [CreateChatUserModel] = []
    private var isSearching: Bool = false
    private var actionItems: [ActionItemEnum] = []
    
    // MARK: - Initialize

    init(view: CreateChatPresenterDelegate,
         interactor: CreateChatInteractorProtocol,
         router: CreateChatRouterProtocol) {
        super.init()
        self.view = view
        self.interactor = interactor
        self.router = router
        self.interactor.delegate = self
    }
    
    private func createActionItems() {
        actionItems = ActionItemEnum.allCases
    }
}

// MARK: - CreateChatPresenterProtocol
extension CreateChatPresenter: CreateChatPresenterProtocol {
    var isSearchActive: Bool {
        isSearching
    }
    
    var hasNoSearchResults: Bool {
        filteredContacts.isEmpty
    }
    
    func viewDidLoad() {
        Task {
            await interactor.fetchAllContacts()
        }
        createActionItems()
    }
    
    func actionItem(at index: Int) -> ActionItemEnum? {
        return actionItems[safe: index]
    }
    
    func numberOfSections() -> Int {
        return isSearching ? 1 : CreateChatSection.allCases.count
    }

    func numberOfRows(in section: Int) -> Int {
        if isSearching {
            return filteredContacts.count
        } else {
            return section == 0 ? ActionItemEnum.allCases.count : allContacts.count
        }
    }
    
    func cellForRow(
        at indexPath: IndexPath,
        in tableView: UITableView
    ) -> UITableViewCell {
        
        if isSearching {
            let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
            let contact = filteredContacts[safe: indexPath.row]
            cell.configure(
                name: contact?.name ?? "",
                role: contact?.summary ?? "",
                imageUrl: contact?.imageUrl ?? "",
                isGroupSelection: false
            )
            return cell
        } else {
            switch CreateChatSection(rawValue: indexPath.section) {
            case .actions:
                let cell = ActionCell.dequeue(from: tableView, at: indexPath)
                cell.configure(delegate: self, item: actionItem(at: indexPath.row))
                return cell
            case .contracts:
                let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
                let contact = allContacts[indexPath.row]
                cell.configure(
                    name: contact.name,
                    role: "",
                    imageUrl: contact.imageUrl,
                    isGroupSelection: false
                )
                return cell
            default:
                return UITableViewCell()
            }
        }
    }
    
    func heightForRowAt(
        at indexPath: IndexPath,
        in tableView: UITableView
    ) -> CGFloat {
        if isSearching {
            return UITableView.automaticDimension
        }
        switch CreateChatSection(rawValue: indexPath.section) {
        case .actions:
            return 56
        default:
            return UITableView.automaticDimension
        }
    }
    
    func titleForHeaderInSection(
        _ tableView: UITableView,
        heightForHeaderInSection section: Int
    ) -> String? {
        guard !isSearching else { return nil }

        if section == CreateChatSection.contracts.rawValue {
            return "Kişiler"
        }
        return nil
    }
    
    
    func heightForHeaderInSection(
        _ tableView: UITableView,
        heightForHeaderInSection section: Int
    ) -> CGFloat {
        guard !isSearching else { return .leastNonzeroMagnitude }
        
        switch CreateChatSection(rawValue: section) {
        case .contracts:
            return 36
        default:
            return .leastNonzeroMagnitude
        }
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
    
    func didSelect(indexPath: IndexPath) {
        let sourceContacts = isSearching ? filteredContacts : allContacts
        
        if let item = sourceContacts[safe: indexPath.row] {
            Task {
                await interactor.createGroup(
                    name: item.name,
                    selectedUserId: item.id,
                    groupImagePath: item.imageUrl,
                    userName: item.username
                )
            }
        }
    }
}

// MARK: - CreateChatInteractorDelegate
extension CreateChatPresenter: CreateChatInteractorDelegate {
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
    
    func didCreateGroupSuccess(response: GroupChatResponseModel, userName: String) {
        DispatchQueue.main.async {
            self.router.handleRouter(
                .chat(
                    userId: response.id,
                    displayName: userName,
                    image: response.image,
                    isGroup: false,
                    isNewCreated: true,
                    toUserId: response.toUserId
                )
            )
        }
    }
}

extension CreateChatPresenter: ActionCellDelegate {
    func didTapAction(item: ActionItemEnum) {
        self.router.handleRouter(.createGroup)
    }
}
