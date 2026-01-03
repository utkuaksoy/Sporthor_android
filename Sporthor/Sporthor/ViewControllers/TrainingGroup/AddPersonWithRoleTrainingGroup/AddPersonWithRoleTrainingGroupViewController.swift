//
//  AddPersonWithRoleTrainingGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.10.2025.
//
//

import UIKit
import ComponentKit

final class AddPersonWithRoleTrainingGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: AddPersonWithRoleTrainingGroupPresenterProtocol {
        get { return self.basePresenter as! AddPersonWithRoleTrainingGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(delegate: self,
                                    textColor: DesignKitColorName.contentStrong900.color,
                                    placeholder: "Kişi Ara",
                                    placeholderColor: DesignKitColorName.contentSoft600.color,
                                    backgroundColor: DesignKitColorName.contentWeak100.color,
                                    cornerRadius: 22,
                                    borderWidth: 1,
                                    selectedBorderColor: DesignKitColorName.borderSub300.color,
                                    font: .body04Compact,
                                    image: Asset.searchbarSearch.image,
                                    clearImage: Asset.searchbarClose.image)
        searchBar.heightAnchor.constraint(equalToConstant: 44).isActive = true
        return searchBar
    }()
    
    private lazy var headerStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 16, left: 16, bottom: 0, right: 16)
        stackView.addArrangedSubviews([searchBar])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.backgroundColor = .clear
        tableView.separatorStyle = .none
        tableView.contentInset = .init(top: 16, left: 0, bottom: 16, right: 0)
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.tableFooterView = UIView()
        tableView.sectionFooterHeight = 0
        return tableView
    }()
    
    private lazy var continueButton: CKButton = {
        let button = CKButton(delegate: self,
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    private lazy var bottomStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 0, left: 16, bottom: 32, right: 16)
        stackView.addArrangedSubviews([continueButton])
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - AddPersonWithRoleTrainingGroupPresenterDelegate
extension AddPersonWithRoleTrainingGroupViewController: AddPersonWithRoleTrainingGroupPresenterDelegate {
    func setupView() {
        view.addSubview(headerStackView)
        view.addSubview(tableView)
        view.addSubview(bottomStackView)
        
        NSLayoutConstraint.activate([
            headerStackView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor),
            headerStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            headerStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            tableView.topAnchor.constraint(equalTo: headerStackView.bottomAnchor),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            bottomStackView.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor),
            bottomStackView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            bottomStackView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            bottomStackView.topAnchor.constraint(equalTo: tableView.bottomAnchor)
        ])
    }
    
    func prepareContinueButtonTitle(_ title: String) {
        continueButton.setTitle(title)
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - CKSearchBarDelegate
extension AddPersonWithRoleTrainingGroupViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.search(text)
    }
    
    func searchBarDidCancel(_ searchBar: CKSearchBar) {
        presenter.searchCancel()
    }
}

// MARK: - UITableViewDataSource
extension AddPersonWithRoleTrainingGroupViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch presenter.viewType {
        case .search:
            return presenter.searches.isEmpty ? 1 : presenter.searches.count
        case .selection:
            return presenter.selectedSearches.isEmpty ? 1 : presenter.selectedSearches.count
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let isSearchMode = presenter.viewType == .search
        let list = isSearchMode ? presenter.searches : presenter.selectedSearches

        guard !list.isEmpty else {
            let cell = EmptyTableViewCell.dequeue(from: tableView, at: indexPath)
            let description = isSearchMode
                ? "Arama sonuçları bulunamadı. Lütfen farklı bir arama yapınız."
            : presenter.role == .person ? "Henüz seçili sporcu yok. Antreman grubunuza sporcu eklemek için arama yapabilirsiniz." : "Henüz seçili antrenör yok. Antreman grubunuza antrenör eklemek için arama yapabilirsiniz."
            cell.bind(image: Asset.infoAlert.image, description: description)
            return cell
        }
        
        switch presenter.role {
        case .technicalStaff:
            let model = list[indexPath.row]
            let cell = UserAndButtonTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.configure(delegate: self,
                           image: model.image,
                           name: model.name,
                           role: model.summary,
                           buttonTitle: model.id == ApplicationContext.shared.userId ? "" : model.isSelected ? "Çıkar" : "Ekle",
                           buttonImage: model.isSelected ? Asset.closeMini.image : Asset.blackPlus.image,
                           followingFriendImage: nil,
                           tag: indexPath.row)
            return cell
        case .person:
            let model = list[indexPath.row]

            let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      name: model.userName,
                      role: model.summary,
                      imageUrl: model.image,
                      isGroupSelection: true,
                      isSelected: model.isSelected,
                      followingFriendImage: nil,
                      tag: indexPath.row)
            return cell
        }
    }
}

// MARK: - UITableViewDelegate
extension AddPersonWithRoleTrainingGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch presenter.role {
        case .person:
            presenter.didSelectRowAt(indexPath)
        default:
            break
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = CKHeaderView(textColor: DesignKitColorName.contentStrong900.color,
                                font: .bold04Compact,
                                leadingCons: 16,
                                trailingCons: -16)
        if presenter.isLogin && !presenter.isEdit {
            if presenter.isFollowing {
                view.updateText("Takip Ettiklerim")
            } else {
                switch presenter.role {
                case .technicalStaff:
                    view.updateText("Antrenörler")
                case .person:
                    view.updateText("Sporcular")
                }
            }
        } else {
            switch presenter.role {
            case .technicalStaff:
                view.updateText("Antrenörler")
            case .person:
                view.updateText("Sporcular")
            }
        }
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        if presenter.selectedSearches.isEmpty {
            return 0
        } else {
            switch presenter.viewType {
            case .selection:
                return 18
            default:
                return 0
            }
        }
    }
}

// MARK: - CKButtonDelegate
extension AddPersonWithRoleTrainingGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedCKButton()
    }
}

// MARK: - UserAndButtonTableViewCellDelegate
extension AddPersonWithRoleTrainingGroupViewController: UserAndButtonTableViewCellDelegate {
    func didTappedAddButtonUserAndButtonTableViewCell(_ tag: Int) {
        presenter.addTechnicalStaff(tag)
    }
}

extension AddPersonWithRoleTrainingGroupViewController: ChatUserCellDelegate {
}
        


