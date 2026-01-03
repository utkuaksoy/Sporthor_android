//
//  CalendarAddPersonOrGroupViewController.swift
//  Sporthor
//
//  Created by derTurke on 29.05.2025.
//
//

import UIKit
import ComponentKit
import PanModal

final class CalendarAddPersonOrGroupViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarAddPersonOrGroupPresenterProtocol {
        get { return self.basePresenter as! CalendarAddPersonOrGroupPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        view.translatesAutoresizingMaskIntoConstraints = false
        return view
    }()
    
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(text: "Kişi/Topluluk Ekle", textColor: .black, textAlignment: .center , font: .heading06)
        label.translatesAutoresizingMaskIntoConstraints = false
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let tableView = UITableView(frame: .zero, style: .grouped)
        tableView.delegate = self
        tableView.dataSource = self
        tableView.translatesAutoresizingMaskIntoConstraints = false
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 16, right: 0)
        tableView.separatorStyle = .none
        tableView.backgroundColor = .clear
        return tableView
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(
            delegate: self,
            title: "Kaydet",
            titleColor: DesignKitColorName.contentStrong900.color,
            buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
            cornerRadius: 23,
            font: .bold03Compact,
            tag: 1)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        button.translatesAutoresizingMaskIntoConstraints = false
        return button
    }()
    
    private lazy var searchBar: CKSearchBar = {
        let searchBar = CKSearchBar(
            delegate: self,
            textColor: DesignKitColorName.contentStrong900.color,
            placeholder: "Kişi ara",
            placeholderColor: DesignKitColorName.contentSoft600.color,
            backgroundColor: DesignKitColorName.contentWeak100.color,
            cornerRadius: 22,
            borderWidth: 1,
            selectedBorderColor: DesignKitColorName.borderSub300.color,
            font: .body04Compact,
            image: Asset.searchbarSearch.image,
            clearImage: Asset.searchbarClose.image
        )
        searchBar.translatesAutoresizingMaskIntoConstraints = false
        return searchBar
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
}

// MARK: - CalendarAddPersonOrGroupPresenterDelegate
extension CalendarAddPersonOrGroupViewController: CalendarAddPersonOrGroupPresenterDelegate {
    func prepareUI() {
        view.addSubview(scrollLineView)
        view.addSubview(titleLabel)
        view.addSubview(searchBar)
        view.addSubview(tableView)
        view.addSubview(submitButton)
        
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            titleLabel.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            titleLabel.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            titleLabel.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            
            searchBar.topAnchor.constraint(equalTo: titleLabel.bottomAnchor, constant: 24),
            searchBar.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            searchBar.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            searchBar.heightAnchor.constraint(equalToConstant: 44),
            
            tableView.topAnchor.constraint(equalTo: searchBar.bottomAnchor, constant: 16),
            tableView.leadingAnchor.constraint(equalTo: view.leadingAnchor),
            tableView.trailingAnchor.constraint(equalTo: view.trailingAnchor),
            
            submitButton.topAnchor.constraint(equalTo: tableView.bottomAnchor),
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -32)
        ])
    }
    
    func reloadData() {
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.tableView.reloadData()
        }
    }
}

// MARK: - CKSearchBarDelegate
extension CalendarAddPersonOrGroupViewController: CKSearchBarDelegate {
    func searchBarTextDidChange(_ searchBar: CKSearchBar, text: String) {
        presenter.searchBarTextDidChange(text)
    }
}

// MARK: - UITableViewDataSource
extension CalendarAddPersonOrGroupViewController: UITableViewDataSource {
    func numberOfSections(in tableView: UITableView) -> Int {
        2
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        switch section {
        case 0:
            return presenter.groups.count
        case 1:
            return presenter.filteredUsers.count
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        switch indexPath.section {
        case 0:
            let cell = SelectedTeamTableViewCell.dequeue(from: tableView, at: indexPath)
            cell.bind(delegate: self,
                      model: presenter.groups[indexPath.row],
                      selectedButtonTitle: "Gönder", topCons: 8, bottomCons: -8)
            return cell
        case 1:
            let user = presenter.filteredUsers[indexPath.row]
            let cell = ChatUserCell.dequeue(from: tableView, at: indexPath)
            cell.bind(
                name: user.name,
                role: user.username,
                imageUrl: user.imageUrl,
                isGroupSelection: true,
                isSelected: user.isSelected
            )
            return cell
        default:
            return UITableViewCell()
        }
    }
}

// MARK: - UITableViewDelegate
extension CalendarAddPersonOrGroupViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        presenter.didSelectRowAt(indexPath)
    }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {
        switch section {
        case 0,1:
            return 46
        default:
            return 0
        }
    }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let view = CKHeaderView(
            textColor: DesignKitColorName.contentStrong900.color,
            numberOfLines: 0,
            leadingCons: 16,
            trailingCons: -16
        )
        switch section {
        case 0:
            view.updateText("Topluluklar")
        case 1:
            view.updateText("Kişiler")
        default:
            break
        }
        return view
    }
    
    func tableView(_ tableView: UITableView, heightForFooterInSection section: Int) -> CGFloat {
        switch section {
        case 0:
            return 24
        default:
            return .zero
        }
    }
    
    func tableView(_ tableView: UITableView, viewForFooterInSection section: Int) -> UIView? {
        return CKSeparatorView()
    }
}

// MARK: - PanModalPresentable
extension CalendarAddPersonOrGroupViewController: PanModalPresentable {
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }

    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
    
    var longFormHeight: PanModalHeight {
        return .maxHeight
    }
}

// MARK: - SelectedTeamTableViewCellDelegate
extension CalendarAddPersonOrGroupViewController: SelectedTeamTableViewCellDelegate {
    func selectedTeam(_ model: TeamItemModel) {
        presenter.selectedGroup(model)
    }
}

extension CalendarAddPersonOrGroupViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedSubmitButton()
    }
}
